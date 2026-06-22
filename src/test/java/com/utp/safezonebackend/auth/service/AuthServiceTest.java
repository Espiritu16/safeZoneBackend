package com.utp.safezonebackend.auth.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.auth.dto.request.SolicitudRegistro;
import com.utp.safezonebackend.auth.repository.RefreshTokenRepository;
import com.utp.safezonebackend.configuracion.service.ConfiguracionSeguridadService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.security.ServicioJwt;
import com.utp.safezonebackend.shared.util.HashTextoUtil;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ServicioJwt servicioJwt;

    @Mock
    private HashTextoUtil hashTextoUtil;

    @Mock
    private ConfiguracionSeguridadService configuracionSeguridadService;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registrarUsuarioRechazaCorreoDuplicadoNormalizado() {
        when(usuarioRepository.existePorCorreo("victima@gmail.com")).thenReturn(true);

        SolicitudRegistro solicitud = new SolicitudRegistro(
                "Victima de Prueba",
                " VICTIMA@gmail.com ",
                "Nueva123"
        );

        assertThatThrownBy(() -> authService.registrarUsuario(solicitud))
                .isInstanceOf(ExcepcionNegocio.class)
                .hasMessage("El correo ya se encuentra registrado");

        verify(configuracionSeguridadService, never()).validarContrasenaSegura(any());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
