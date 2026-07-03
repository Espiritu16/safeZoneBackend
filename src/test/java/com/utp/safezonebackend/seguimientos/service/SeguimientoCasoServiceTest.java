package com.utp.safezonebackend.seguimientos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.seguimientos.dto.request.CrearSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.entity.SeguimientoCaso;
import com.utp.safezonebackend.seguimientos.mapper.SeguimientoCasoMapper;
import com.utp.safezonebackend.seguimientos.repository.SeguimientoCasoRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
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
class SeguimientoCasoServiceTest {

    @Mock
    private SeguimientoCasoRepository repository;

    @Mock
    private SeguimientoCasoMapper mapper;

    @Mock
    private CasoRepository casoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SeguimientoCasoService service;

    @AfterEach
    void limpiarSesion() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createUsaUsuarioAutenticadoComoAutor() {
        Caso caso = new Caso();
        caso.setId("caso-1");
        caso.setEstado(EstadoCaso.EN_ATENCION);
        caso.setPrioridad(PrioridadCaso.ALTA);
        caso.setActivo(true);
        Usuario psicologo = usuario("psicologo-1", RolUsuario.PSICOLOGO);
        psicologo.setCorreo("psicologo@safezone.test");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("psicologo@safezone.test", null)
        );

        when(casoRepository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(caso));
        when(usuarioRepository.buscarPorCorreo("psicologo@safezone.test")).thenReturn(Optional.of(psicologo));
        when(repository.save(any(SeguimientoCaso.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(SeguimientoCaso.class))).thenReturn(null);

        service.create(new CrearSeguimientoCasoRequest(
                "caso-1",
                "OBSERVACION",
                "Contenido del seguimiento",
                "Proxima llamada",
                OffsetDateTime.now().plusDays(2)
        ));

        ArgumentCaptor<SeguimientoCaso> captor = ArgumentCaptor.forClass(SeguimientoCaso.class);
        org.mockito.Mockito.verify(repository).save(captor.capture());
        assertThat(captor.getValue().getAutorId()).isEqualTo("psicologo-1");
        assertThat(captor.getValue().getRolAutor()).isEqualTo(RolUsuario.PSICOLOGO);
    }

    @Test
    void createRechazaAutorVictima() {
        Caso caso = new Caso();
        caso.setId("caso-1");
        caso.setEstado(EstadoCaso.EN_ATENCION);
        caso.setPrioridad(PrioridadCaso.ALTA);
        caso.setActivo(true);
        Usuario victima = usuario("victima-1", RolUsuario.VICTIMA);
        victima.setCorreo("victima@safezone.test");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("victima@safezone.test", null)
        );

        when(casoRepository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(caso));
        when(usuarioRepository.buscarPorCorreo("victima@safezone.test")).thenReturn(Optional.of(victima));

        assertThatThrownBy(() -> service.create(new CrearSeguimientoCasoRequest(
                "caso-1",
                "OBSERVACION",
                "Contenido del seguimiento",
                "Proxima llamada",
                OffsetDateTime.now().plusDays(2)
        ))).isInstanceOf(ExcepcionNegocio.class)
                .hasMessageContaining("Solo profesionales autorizados");
    }

    private Usuario usuario(String id, RolUsuario rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(rol);
        usuario.setActivo(true);
        return usuario;
    }
}
