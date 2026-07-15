package com.utp.safezonebackend.asignaciones.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.asignaciones.dto.request.CrearAsignacionCasoRequest;
import com.utp.safezonebackend.asignaciones.dto.response.AsignacionCasoResponse;
import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import com.utp.safezonebackend.asignaciones.mapper.AsignacionCasoMapper;
import com.utp.safezonebackend.asignaciones.repository.AsignacionCasoRepository;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.notificaciones.service.NotificacionService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
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
class AsignacionCasoServiceTest {

    @Mock
    private AsignacionCasoRepository repository;

    @Mock
    private AsignacionCasoMapper mapper;

    @Mock
    private CasoRepository casoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private AsignacionCasoService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createAsignaProfesionalActivoAlCaso() {
        autenticarAdmin();
        when(casoRepository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(new Caso()));
        when(usuarioRepository.findById("psico-1")).thenReturn(Optional.of(usuario("psico-1", RolUsuario.PSICOLOGO)));
        when(usuarioRepository.buscarPorCorreo("admin@safezone.gob.pe")).thenReturn(Optional.of(usuario("admin-1", RolUsuario.ADMIN)));
        when(repository.save(any(AsignacionCaso.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(AsignacionCaso.class))).thenAnswer(invocation -> {
            AsignacionCaso asignacion = invocation.getArgument(0);
            return new AsignacionCasoResponse(
                    asignacion.getId(),
                    asignacion.getCasoId(),
                    asignacion.getProfesionalId(),
                    asignacion.getRolProfesional(),
                    asignacion.isActivo(),
                    asignacion.getFechaAsignacion(),
                    asignacion.getFechaFin(),
                    asignacion.getAsignadoPor(),
                    asignacion.getFechaActualizacion(),
                    asignacion.getActualizadoPor(),
                    asignacion.getInactivadoPor(),
                    asignacion.getFechaInactivacion()
            );
        });

        AsignacionCasoResponse response = service.create(new CrearAsignacionCasoRequest(
                "caso-1",
                "psico-1",
                RolUsuario.PSICOLOGO
        ));

        assertThat(response.casoId()).isEqualTo("caso-1");
        assertThat(response.profesionalId()).isEqualTo("psico-1");
        assertThat(response.rolProfesional()).isEqualTo(RolUsuario.PSICOLOGO);
        assertThat(response.asignadoPor()).isEqualTo("admin-1");

        ArgumentCaptor<AsignacionCaso> captor = ArgumentCaptor.forClass(AsignacionCaso.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().isActivo()).isTrue();
        assertThat(captor.getValue().getFechaAsignacion()).isNotNull();
    }

    @Test
    void createRechazaProfesionalConRolDistinto() {
        when(casoRepository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(new Caso()));
        when(usuarioRepository.findById("def-1")).thenReturn(Optional.of(usuario("def-1", RolUsuario.DEFENSOR)));

        assertThatThrownBy(() -> service.create(new CrearAsignacionCasoRequest(
                "caso-1",
                "def-1",
                RolUsuario.PSICOLOGO
        ))).isInstanceOf(ExcepcionNegocio.class)
                .hasMessageContaining("no corresponde");
    }

    @Test
    void createRechazaRolNoAsignable() {
        when(casoRepository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(new Caso()));

        assertThatThrownBy(() -> service.create(new CrearAsignacionCasoRequest(
                "caso-1",
                "admin-1",
                RolUsuario.ADMIN
        ))).isInstanceOf(ExcepcionNegocio.class)
                .hasMessageContaining("psicologo o defensor");
    }

    private void autenticarAdmin() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin@safezone.gob.pe", "N/A")
        );
    }

    private Usuario usuario(String id, RolUsuario rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setCorreo(id + "@safezone.gob.pe");
        usuario.setRol(rol);
        usuario.setActivo(true);
        return usuario;
    }
}
