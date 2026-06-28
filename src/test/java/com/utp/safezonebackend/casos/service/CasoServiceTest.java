package com.utp.safezonebackend.casos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.casos.dto.request.ActualizarCasoRequest;
import com.utp.safezonebackend.casos.dto.request.CrearCasoRequest;
import com.utp.safezonebackend.casos.dto.response.CasoResponse;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import com.utp.safezonebackend.casos.mapper.CasoMapper;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CasoServiceTest {

    @Mock
    private CasoRepository repository;

    @Mock
    private CasoMapper mapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VictimaAliasRepository victimaAliasRepository;

    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private CasoService service;

    @Test
    void createRegistraCasoEnEstadoRegistrado() {
        Usuario victima = usuario("victima-1", RolUsuario.VICTIMA);
        when(usuarioRepository.findById("victima-1")).thenReturn(Optional.of(victima));
        when(repository.save(any(Caso.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Caso.class))).thenAnswer(invocation -> {
            Caso caso = invocation.getArgument(0);
            return new CasoResponse(caso.getId(), caso.getVictimaId(), caso.getEstado(), caso.getPrioridad(),
                    caso.getResumen(), caso.getDistrito(), caso.isActivo(), caso.getFechaCreacion(),
                    caso.getFechaCierre(), caso.getFechaActualizacion());
        });

        CasoResponse response = service.create(new CrearCasoRequest(
                "victima-1",
                "Caso inicial",
                "Lima",
                PrioridadCaso.ALTA,
                null
        ));

        assertThat(response.estado()).isEqualTo(EstadoCaso.REGISTRADO);
        assertThat(response.victimaId()).isEqualTo("victima-1");
        ArgumentCaptor<Caso> captor = ArgumentCaptor.forClass(Caso.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getFechaCreacion()).isNotNull();
    }

    @Test
    void updateRechazaTransicionDirectaDeRegistradoACerrado() {
        Caso caso = new Caso();
        caso.setId("caso-1");
        caso.setVictimaId("victima-1");
        caso.setEstado(EstadoCaso.REGISTRADO);
        caso.setPrioridad(PrioridadCaso.MEDIA);
        caso.setActivo(true);
        when(repository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(caso));

        assertThatThrownBy(() -> service.update("caso-1", new ActualizarCasoRequest(
                null,
                null,
                null,
                null,
                EstadoCaso.CERRADO
        ))).isInstanceOf(ExcepcionNegocio.class)
                .hasMessageContaining("Transicion de estado no permitida");
    }

    @Test
    void updatePermiteRetornarDeAtencionAEvaluacion() {
        Caso caso = new Caso();
        caso.setId("caso-1");
        caso.setVictimaId("victima-1");
        caso.setEstado(EstadoCaso.EN_ATENCION);
        caso.setPrioridad(PrioridadCaso.MEDIA);
        caso.setResumen("Caso en atencion");
        caso.setDistrito("Comas");
        caso.setActivo(true);
        when(repository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(caso));
        when(repository.save(any(Caso.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Caso.class))).thenAnswer(invocation -> {
            Caso actualizado = invocation.getArgument(0);
            return new CasoResponse(actualizado.getId(), actualizado.getVictimaId(), actualizado.getEstado(), actualizado.getPrioridad(),
                    actualizado.getResumen(), actualizado.getDistrito(), actualizado.isActivo(), actualizado.getFechaCreacion(),
                    actualizado.getFechaCierre(), actualizado.getFechaActualizacion());
        });

        CasoResponse response = service.update("caso-1", new ActualizarCasoRequest(
                null,
                null,
                null,
                null,
                EstadoCaso.EN_EVALUACION
        ));

        assertThat(response.estado()).isEqualTo(EstadoCaso.EN_EVALUACION);
    }

    @Test
    void updatePermiteCerrarCasoActivoSinInactivarlo() {
        Caso caso = new Caso();
        caso.setId("caso-1");
        caso.setVictimaId("victima-1");
        caso.setEstado(EstadoCaso.EN_EVALUACION);
        caso.setPrioridad(PrioridadCaso.MEDIA);
        caso.setResumen("Caso en evaluacion");
        caso.setDistrito("Comas");
        caso.setActivo(true);
        when(repository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(caso));
        when(repository.save(any(Caso.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Caso.class))).thenAnswer(invocation -> {
            Caso actualizado = invocation.getArgument(0);
            return new CasoResponse(actualizado.getId(), actualizado.getVictimaId(), actualizado.getEstado(), actualizado.getPrioridad(),
                    actualizado.getResumen(), actualizado.getDistrito(), actualizado.isActivo(), actualizado.getFechaCreacion(),
                    actualizado.getFechaCierre(), actualizado.getFechaActualizacion());
        });

        CasoResponse response = service.update("caso-1", new ActualizarCasoRequest(
                null,
                null,
                null,
                null,
                EstadoCaso.CERRADO
        ));

        assertThat(response.estado()).isEqualTo(EstadoCaso.CERRADO);
        assertThat(response.activo()).isTrue();
        assertThat(response.fechaCierre()).isNotNull();
    }

    private Usuario usuario(String id, RolUsuario rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(rol);
        usuario.setActivo(true);
        return usuario;
    }
}
