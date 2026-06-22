package com.utp.safezonebackend.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.auth.dto.request.SolicitudRecuperarContrasena;
import com.utp.safezonebackend.auth.dto.request.SolicitudRestablecerContrasena;
import com.utp.safezonebackend.auth.entity.RecuperacionContrasenaCodigo;
import com.utp.safezonebackend.auth.repository.RecuperacionContrasenaCodigoRepository;
import com.utp.safezonebackend.configuracion.service.ConfiguracionSeguridadService;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.shared.util.GeneradorCodigoRecuperacion;
import com.utp.safezonebackend.shared.util.HashTextoUtil;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class RecuperacionContrasenaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RecuperacionContrasenaCodigoRepository codigoRepository;

    @Mock
    private GeneradorCodigoRecuperacion generadorCodigoRecuperacion;

    @Mock
    private HashTextoUtil hashTextoUtil;

    @Mock
    private CorreoService correoService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ConfiguracionSeguridadService configuracionSeguridadService;

    @Mock
    private AuditoriaService auditoriaService;

    @InjectMocks
    private RecuperacionContrasenaService service;

    @Test
    void solicitarCodigoRechazaCorreoInexistenteSinCrearCodigoNiEnviarCorreo() {
        when(usuarioRepository.buscarPorCorreo("noexiste@gmail.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.solicitarCodigoRecuperacion(new SolicitudRecuperarContrasena(" noexiste@gmail.com ")))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("No existe una cuenta registrada con ese correo");

        verify(codigoRepository, never()).save(any(RecuperacionContrasenaCodigo.class));
        verify(correoService, never()).enviarCodigoRecuperacion(any(), any());
    }

    @Test
    void restablecerContrasenaSoloActualizaUsuarioDelCorreoNormalizado() {
        Usuario victima = usuario("victima-1", "victima@gmail.com");
        RecuperacionContrasenaCodigo codigo = codigoActivo("victima-1", "hash-123");

        when(usuarioRepository.buscarPorCorreo("victima@gmail.com")).thenReturn(Optional.of(victima));
        when(codigoRepository.findTopByUsuarioIdAndActivoTrueAndUsadoFalseOrderByFechaCreacionDesc("victima-1"))
                .thenReturn(Optional.of(codigo));
        when(hashTextoUtil.hashearSha256("123456")).thenReturn("hash-123");
        when(passwordEncoder.encode("Nueva123")).thenReturn("hash-nuevo");

        service.restablecerContrasena(new SolicitudRestablecerContrasena(" VICTIMA@gmail.com ", "123456", "Nueva123"));

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertThat(usuarioCaptor.getValue().getId()).isEqualTo("victima-1");
        assertThat(usuarioCaptor.getValue().getCorreo()).isEqualTo("victima@gmail.com");
        assertThat(usuarioCaptor.getValue().getContrasenaHash()).isEqualTo("hash-nuevo");
        assertThat(codigo.isUsado()).isTrue();
        assertThat(codigo.isActivo()).isFalse();
    }

    private Usuario usuario(String id, String correo) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setCorreo(correo);
        usuario.setRol(RolUsuario.VICTIMA);
        usuario.setActivo(true);
        return usuario;
    }

    private RecuperacionContrasenaCodigo codigoActivo(String usuarioId, String codigoHash) {
        RecuperacionContrasenaCodigo codigo = new RecuperacionContrasenaCodigo();
        codigo.setId("codigo-1");
        codigo.setUsuarioId(usuarioId);
        codigo.setCodigoHash(codigoHash);
        codigo.setExpiraEn(LocalDateTime.now().plusMinutes(15));
        codigo.setUsado(false);
        codigo.setActivo(true);
        codigo.setIntentos(0);
        codigo.setMaxIntentos(5);
        codigo.setFechaCreacion(LocalDateTime.now());
        return codigo;
    }
}
