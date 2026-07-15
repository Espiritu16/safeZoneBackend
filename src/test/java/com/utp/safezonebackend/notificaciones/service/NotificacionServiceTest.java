package com.utp.safezonebackend.notificaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.citas.entity.Cita;
import com.utp.safezonebackend.citas.enums.TipoCita;
import com.utp.safezonebackend.notificaciones.dto.request.CrearNotificacionRequest;
import com.utp.safezonebackend.notificaciones.dto.response.NotificacionResponse;
import com.utp.safezonebackend.notificaciones.entity.Notificacion;
import com.utp.safezonebackend.notificaciones.enums.PrioridadNotificacion;
import com.utp.safezonebackend.notificaciones.enums.TipoNotificacion;
import com.utp.safezonebackend.notificaciones.mapper.NotificacionMapper;
import com.utp.safezonebackend.notificaciones.repository.NotificacionRepository;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @Mock
    private NotificacionMapper mapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private NotificacionService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createGuardaNotificacionActivaNoLeida() {
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Notificacion.class))).thenAnswer(invocation -> response(invocation.getArgument(0)));

        NotificacionResponse response = service.create(new CrearNotificacionRequest(
                "usuario-1",
                "caso-1",
                null,
                TipoNotificacion.SISTEMA,
                PrioridadNotificacion.ALTA,
                "Nueva asignacion",
                "Se asigno un caso"
        ));

        assertThat(response.usuarioId()).isEqualTo("usuario-1");
        assertThat(response.leida()).isFalse();
        assertThat(response.activo()).isTrue();
    }

    @Test
    void notificarCitaProximaCreaRecordatorioParaVictimaYProfesional() {
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Cita cita = new Cita();
        cita.setId("cita-1");
        cita.setCasoId("caso-1");
        cita.setVictimaId("victima-1");
        cita.setEspecialistaId("prof-1");
        cita.setTipoCita(TipoCita.PSICOLOGIA);
        cita.setFechaInicio(OffsetDateTime.parse("2026-07-16T09:00:00-05:00"));

        service.notificarCitaProxima(cita);

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(repository, org.mockito.Mockito.times(2)).save(captor.capture());
        assertThat(captor.getAllValues())
                .extracting(Notificacion::getUsuarioId)
                .containsExactlyInAnyOrder("victima-1", "prof-1");
        assertThat(captor.getAllValues())
                .allMatch(notificacion -> notificacion.getTipo() == TipoNotificacion.RECORDATORIO);
    }

    @Test
    void findAllAutenticadoListaSoloNotificacionesDelUsuarioActual() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("victima@safezone.gob.pe", null)
        );
        Usuario usuario = new Usuario();
        usuario.setId("victima-1");
        usuario.setCorreo("victima@safezone.gob.pe");
        usuario.setRol(RolUsuario.VICTIMA);
        usuario.setActivo(true);
        Notificacion notificacion = notificacion("victima-1");
        when(usuarioRepository.buscarPorCorreo("victima@safezone.gob.pe")).thenReturn(Optional.of(usuario));
        when(repository.findByUsuarioIdAndActivoTrueOrderByFechaCreacionDesc("victima-1"))
                .thenReturn(List.of(notificacion));
        when(mapper.toResponse(notificacion)).thenReturn(response(notificacion));

        List<NotificacionResponse> response = service.findAllAutenticado();

        org.assertj.core.api.Assertions.assertThat(response)
                .hasSize(1)
                .first()
                .extracting(NotificacionResponse::usuarioId)
                .isEqualTo("victima-1");
    }

    @Test
    void notificarPredenunciaFormalizadaCreaNotificacionDeAltaPrioridad() {
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.notificarPredenunciaFormalizada("victima-1", "caso-1", "denuncia-1");

        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(repository).save(captor.capture());
        Notificacion guardada = captor.getValue();
        org.assertj.core.api.Assertions.assertThat(guardada.getUsuarioId()).isEqualTo("victima-1");
        org.assertj.core.api.Assertions.assertThat(guardada.getCasoId()).isEqualTo("caso-1");
        org.assertj.core.api.Assertions.assertThat(guardada.getDenunciaId()).isEqualTo("denuncia-1");
        org.assertj.core.api.Assertions.assertThat(guardada.getPrioridad()).isEqualTo(PrioridadNotificacion.ALTA);
    }

    @Test
    void notificarPredenunciaEnContactoIgnoraPredenunciaSinVictimaVinculada() {
        service.notificarPredenunciaEnContacto(null, "predenuncia-1");

        verify(repository, never()).save(any());
    }

    private Notificacion notificacion(String usuarioId) {
        Notificacion notificacion = new Notificacion();
        notificacion.setId("notificacion-1");
        notificacion.setUsuarioId(usuarioId);
        notificacion.setTipo(TipoNotificacion.SISTEMA);
        notificacion.setPrioridad(PrioridadNotificacion.MEDIA);
        notificacion.setTitulo("Titulo");
        notificacion.setMensaje("Mensaje");
        notificacion.setActivo(true);
        notificacion.setLeida(false);
        notificacion.setFechaCreacion(OffsetDateTime.now());
        notificacion.setFechaActualizacion(OffsetDateTime.now());
        return notificacion;
    }

    private NotificacionResponse response(Notificacion notificacion) {
        return new NotificacionResponse(
                notificacion.getId(),
                notificacion.getUsuarioId(),
                notificacion.getCasoId(),
                notificacion.getDenunciaId(),
                notificacion.getTipo(),
                notificacion.getPrioridad(),
                notificacion.getTitulo(),
                notificacion.getMensaje(),
                notificacion.isLeida(),
                notificacion.getFechaLectura(),
                notificacion.isActivo(),
                notificacion.getFechaCreacion(),
                notificacion.getFechaActualizacion()
        );
    }
}
