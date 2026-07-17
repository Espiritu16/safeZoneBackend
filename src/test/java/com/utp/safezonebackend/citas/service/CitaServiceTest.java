package com.utp.safezonebackend.citas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import com.utp.safezonebackend.asignaciones.repository.AsignacionCasoRepository;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.citas.dto.request.ActualizarCitaRequest;
import com.utp.safezonebackend.citas.dto.request.CrearCitaRequest;
import com.utp.safezonebackend.citas.dto.response.CitaResponse;
import com.utp.safezonebackend.citas.entity.Cita;
import com.utp.safezonebackend.citas.enums.EstadoCita;
import com.utp.safezonebackend.citas.enums.TipoCita;
import com.utp.safezonebackend.citas.mapper.CitaMapper;
import com.utp.safezonebackend.citas.repository.CitaRepository;
import com.utp.safezonebackend.notificaciones.service.NotificacionService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository repository;

    @Mock
    private CitaMapper mapper;

    @Mock
    private CasoRepository casoRepository;

    @Mock
    private AsignacionCasoRepository asignacionCasoRepository;

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private CitaService service;

    @Test
    void createProgramaCitaConProfesionalAsignadoDisponible() {
        OffsetDateTime inicio = OffsetDateTime.parse("2026-07-16T09:00:00-05:00");
        when(casoRepository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(caso("caso-1", "victima-1")));
        when(asignacionCasoRepository.findTopByCasoIdAndRolProfesionalAndActivoTrueOrderByFechaAsignacionDesc("caso-1", RolUsuario.PSICOLOGO))
                .thenReturn(Optional.of(asignacion("prof-1", RolUsuario.PSICOLOGO)));
        when(repository.findSolapadasPorEspecialista("prof-1", inicio, inicio.plusMinutes(60), List.of(EstadoCita.CANCELADA)))
                .thenReturn(List.of());
        when(repository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Cita.class))).thenAnswer(invocation -> response(invocation.getArgument(0)));

        CitaResponse response = service.create(new CrearCitaRequest(
                "caso-1",
                TipoCita.PSICOLOGIA,
                inicio,
                null,
                "Primera sesion"
        ));

        assertThat(response.casoId()).isEqualTo("caso-1");
        assertThat(response.victimaId()).isEqualTo("victima-1");
        assertThat(response.especialistaId()).isEqualTo("prof-1");
        assertThat(response.estado()).isEqualTo(EstadoCita.PROGRAMADA);
        assertThat(response.reprogramada()).isFalse();
        verify(notificacionService).notificarCitaProxima(any(Cita.class));
    }

    @Test
    void createRechazaCitaCuandoProfesionalNoTieneDisponibilidad() {
        OffsetDateTime inicio = OffsetDateTime.parse("2026-07-16T09:00:00-05:00");
        Cita existente = new Cita();
        existente.setId("cita-existente");
        existente.setEstado(EstadoCita.PROGRAMADA);
        when(casoRepository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(caso("caso-1", "victima-1")));
        when(asignacionCasoRepository.findTopByCasoIdAndRolProfesionalAndActivoTrueOrderByFechaAsignacionDesc("caso-1", RolUsuario.DEFENSOR))
                .thenReturn(Optional.of(asignacion("prof-1", RolUsuario.DEFENSOR)));
        when(repository.findSolapadasPorEspecialista("prof-1", inicio, inicio.plusMinutes(60), List.of(EstadoCita.CANCELADA)))
                .thenReturn(List.of(existente));

        assertThatThrownBy(() -> service.create(new CrearCitaRequest(
                "caso-1",
                TipoCita.LEGAL,
                inicio,
                null,
                null
        ))).isInstanceOf(ExcepcionNegocio.class)
                .hasMessageContaining("disponibilidad");
    }

    @Test
    void updatePermiteConfirmarAtencionNoAsistida() {
        Cita cita = new Cita();
        cita.setId("cita-1");
        cita.setCasoId("caso-1");
        cita.setVictimaId("victima-1");
        cita.setEspecialistaId("prof-1");
        cita.setTipoCita(TipoCita.LEGAL);
        cita.setFechaInicio(OffsetDateTime.parse("2026-07-16T09:00:00-05:00"));
        cita.setFechaFin(OffsetDateTime.parse("2026-07-16T10:00:00-05:00"));
        cita.setEstado(EstadoCita.PROGRAMADA);
        cita.setActivo(true);
        when(repository.findByIdAndActivoTrue("cita-1")).thenReturn(Optional.of(cita));
        when(repository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Cita.class))).thenAnswer(invocation -> response(invocation.getArgument(0)));

        CitaResponse response = service.update("cita-1", new ActualizarCitaRequest(
                null,
                null,
                null,
                EstadoCita.NO_ASISTIO,
                "No respondio",
                "Se llamo dos veces"
        ));

        assertThat(response.estado()).isEqualTo(EstadoCita.NO_ASISTIO);
        assertThat(response.motivoCancelacion()).isEqualTo("No respondio");
        ArgumentCaptor<Cita> captor = ArgumentCaptor.forClass(Cita.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getFechaActualizacion()).isNotNull();
    }

    @Test
    void updateMarcaCitaReprogramadaCuandoCambiaHorario() {
        OffsetDateTime inicioOriginal = OffsetDateTime.parse("2026-07-16T09:00:00-05:00");
        OffsetDateTime inicioNuevo = OffsetDateTime.parse("2026-07-17T11:00:00-05:00");
        Cita cita = new Cita();
        cita.setId("cita-1");
        cita.setCasoId("caso-1");
        cita.setVictimaId("victima-1");
        cita.setEspecialistaId("prof-1");
        cita.setTipoCita(TipoCita.PSICOLOGIA);
        cita.setFechaInicio(inicioOriginal);
        cita.setFechaFin(inicioOriginal.plusHours(1));
        cita.setEstado(EstadoCita.CONFIRMADA);
        cita.setActivo(true);
        when(repository.findByIdAndActivoTrue("cita-1")).thenReturn(Optional.of(cita));
        when(asignacionCasoRepository.findTopByCasoIdAndRolProfesionalAndActivoTrueOrderByFechaAsignacionDesc("caso-1", RolUsuario.PSICOLOGO))
                .thenReturn(Optional.of(asignacion("prof-1", RolUsuario.PSICOLOGO)));
        when(repository.findSolapadasPorEspecialista("prof-1", inicioNuevo, inicioNuevo.plusHours(1), List.of(EstadoCita.CANCELADA)))
                .thenReturn(List.of());
        when(repository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Cita.class))).thenAnswer(invocation -> response(invocation.getArgument(0)));

        CitaResponse response = service.update("cita-1", new ActualizarCitaRequest(
                TipoCita.PSICOLOGIA,
                inicioNuevo,
                inicioNuevo.plusHours(1),
                null,
                null,
                "Nuevo horario coordinado"
        ));

        assertThat(response.estado()).isEqualTo(EstadoCita.PROGRAMADA);
        assertThat(response.reprogramada()).isTrue();
        verify(notificacionService).notificarCitaProxima(any(Cita.class));
    }

    private Caso caso(String id, String victimaId) {
        Caso caso = new Caso();
        caso.setId(id);
        caso.setVictimaId(victimaId);
        caso.setActivo(true);
        return caso;
    }

    private AsignacionCaso asignacion(String profesionalId, RolUsuario rol) {
        AsignacionCaso asignacion = new AsignacionCaso();
        asignacion.setProfesionalId(profesionalId);
        asignacion.setRolProfesional(rol);
        asignacion.setActivo(true);
        return asignacion;
    }

    private CitaResponse response(Cita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getCasoId(),
                cita.getVictimaId(),
                cita.getEspecialistaId(),
                cita.getTipoCita(),
                cita.getFechaInicio(),
                cita.getFechaFin(),
                cita.getEstado(),
                cita.getMotivoCancelacion(),
                cita.getObservaciones(),
                cita.isReprogramada(),
                cita.isActivo(),
                cita.getFechaCreacion(),
                cita.getFechaActualizacion()
        );
    }
}
