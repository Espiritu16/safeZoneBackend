package com.utp.safezonebackend.usuarios.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.configuracion.service.ConfiguracionSeguridadService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.dto.request.CambiarContrasenaRequest;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.mapper.UsuarioMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private ConfiguracionSeguridadService configuracionSeguridadService;

    @InjectMocks
    private UsuarioService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void cambiarContrasenaAutenticadoActualizaHashCuandoLaSolicitudEsValida() {
        autenticar("victima@safezone.gob.pe");
        Usuario usuario = usuarioActivo();
        when(repository.buscarPorCorreo("victima@safezone.gob.pe")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("Actual123", "hash-actual")).thenReturn(true);
        when(passwordEncoder.matches("Nueva123", "hash-actual")).thenReturn(false);
        when(passwordEncoder.encode("Nueva123")).thenReturn("hash-nuevo");
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.cambiarContrasenaAutenticado(new CambiarContrasenaRequest(
                "Actual123",
                "Nueva123",
                "Nueva123"
        ));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(configuracionSeguridadService).validarContrasenaSegura("Nueva123");
        verify(repository).save(captor.capture());
        Usuario guardado = captor.getValue();
        org.assertj.core.api.Assertions.assertThat(guardado.getContrasenaHash()).isEqualTo("hash-nuevo");
        org.assertj.core.api.Assertions.assertThat(guardado.getActualizadoPor()).isEqualTo("usuario-1");
    }

    @Test
    void cambiarContrasenaAutenticadoRechazaContrasenaActualIncorrecta() {
        autenticar("victima@safezone.gob.pe");
        when(repository.buscarPorCorreo("victima@safezone.gob.pe")).thenReturn(Optional.of(usuarioActivo()));
        when(passwordEncoder.matches("Mala123", "hash-actual")).thenReturn(false);

        assertThatThrownBy(() -> service.cambiarContrasenaAutenticado(new CambiarContrasenaRequest(
                "Mala123",
                "Nueva123",
                "Nueva123"
        )))
                .isInstanceOf(ExcepcionNegocio.class)
                .hasMessage("La contrasena actual no es correcta");

        verify(repository, never()).save(any());
    }

    @Test
    void cambiarContrasenaAutenticadoRechazaConfirmacionDistinta() {
        autenticar("victima@safezone.gob.pe");
        when(repository.buscarPorCorreo("victima@safezone.gob.pe")).thenReturn(Optional.of(usuarioActivo()));
        when(passwordEncoder.matches("Actual123", "hash-actual")).thenReturn(true);

        assertThatThrownBy(() -> service.cambiarContrasenaAutenticado(new CambiarContrasenaRequest(
                "Actual123",
                "Nueva123",
                "Otra123"
        )))
                .isInstanceOf(ExcepcionNegocio.class)
                .hasMessage("La confirmacion de contrasena no coincide");

        verify(repository, never()).save(any());
    }

    private void autenticar(String correo) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(correo, null)
        );
    }

    private Usuario usuarioActivo() {
        Usuario usuario = new Usuario();
        usuario.setId("usuario-1");
        usuario.setCorreo("victima@safezone.gob.pe");
        usuario.setContrasenaHash("hash-actual");
        usuario.setNombres("Maria");
        usuario.setApellidos("Torres");
        usuario.setDni("12345678");
        usuario.setRol(RolUsuario.VICTIMA);
        usuario.setActivo(true);
        return usuario;
    }
}
