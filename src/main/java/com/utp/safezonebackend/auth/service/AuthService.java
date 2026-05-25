package com.utp.safezonebackend.auth.service;

import com.utp.safezonebackend.auth.dto.request.SolicitudLogin;
import com.utp.safezonebackend.auth.dto.request.SolicitudRegistro;
import com.utp.safezonebackend.auth.dto.response.RespuestaBasica;
import com.utp.safezonebackend.auth.dto.response.RespuestaLogin;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RespuestaBasica registrarUsuario(SolicitudRegistro solicitud) {
        if (usuarioRepository.existePorCorreo(solicitud.correo())) {
            throw new ExcepcionNegocio("El correo ya se encuentra registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID().toString());
        usuario.setCorreo(solicitud.correo().trim().toLowerCase());
        usuario.setContrasenaHash(passwordEncoder.encode(solicitud.password()));
        usuario.setNombres(solicitud.nombre().trim());
        usuario.setApellidos("N/A");
        usuario.setDni(generarDniTemporalUnico());
        usuario.setRol(RolUsuario.VICTIMA);
        usuario.setActivo(true);
        usuario.setFechaCreacion(OffsetDateTime.now());
        usuario.setFechaActualizacion(OffsetDateTime.now());
        usuarioRepository.save(usuario);

        return new RespuestaBasica(true, "Cuenta creada correctamente");
    }

    @Transactional(readOnly = true)
    public RespuestaLogin iniciarSesion(SolicitudLogin solicitud) {
        Usuario usuario = usuarioRepository.buscarPorCorreo(solicitud.correo())
                .orElseThrow(() -> new RecursoNoEncontradoException("El correo no existe"));

        if (!usuario.isActivo()) {
            throw new ExcepcionNegocio("El usuario no se encuentra habilitado");
        }

        boolean contrasenaValida = passwordEncoder.matches(solicitud.password(), usuario.getContrasenaHash());
        if (!contrasenaValida) {
            throw new ExcepcionNegocio("Credenciales invalidas");
        }

        return new RespuestaLogin(
                true,
                "Inicio de sesion correcto",
                usuario.getId(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getRol().name(),
                null
        );
    }

    private String generarDniTemporalUnico() {
        String dni;
        do {
            int numero = ThreadLocalRandom.current().nextInt(10_000_000, 99_999_999);
            dni = String.valueOf(numero);
        } while (usuarioRepository.existsByDni(dni));
        return dni;
    }
}
