package com.utp.safezonebackend.auth.service;

import com.utp.safezonebackend.auditoria.dto.request.RegistroAuditoriaInterna;
import com.utp.safezonebackend.auditoria.enums.ResultadoAuditoria;
import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.auth.dto.request.SolicitudCerrarSesion;
import com.utp.safezonebackend.auth.dto.request.SolicitudLogin;
import com.utp.safezonebackend.auth.dto.request.SolicitudRegistro;
import com.utp.safezonebackend.auth.dto.request.SolicitudRenovarToken;
import com.utp.safezonebackend.auth.dto.response.RespuestaBasica;
import com.utp.safezonebackend.auth.dto.response.RespuestaLogin;
import com.utp.safezonebackend.auth.dto.response.RespuestaRenovarToken;
import com.utp.safezonebackend.auth.entity.RefreshToken;
import com.utp.safezonebackend.auth.repository.RefreshTokenRepository;
import com.utp.safezonebackend.configuracion.service.ConfiguracionSeguridadService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.shared.security.ServicioJwt;
import com.utp.safezonebackend.shared.util.HashTextoUtil;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicioJwt servicioJwt;
    private final HashTextoUtil hashTextoUtil;
    private final ConfiguracionSeguridadService configuracionSeguridadService;
    private final AuditoriaService auditoriaService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            ServicioJwt servicioJwt,
            HashTextoUtil hashTextoUtil,
            ConfiguracionSeguridadService configuracionSeguridadService,
            AuditoriaService auditoriaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.servicioJwt = servicioJwt;
        this.hashTextoUtil = hashTextoUtil;
        this.configuracionSeguridadService = configuracionSeguridadService;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public RespuestaBasica registrarUsuario(SolicitudRegistro solicitud) {
        if (usuarioRepository.existePorCorreo(solicitud.correo())) {
            throw new ExcepcionNegocio("El correo ya se encuentra registrado");
        }
        configuracionSeguridadService.validarContrasenaSegura(solicitud.contrasena());

        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID().toString());
        usuario.setCorreo(solicitud.correo().trim().toLowerCase());
        usuario.setContrasenaHash(passwordEncoder.encode(solicitud.contrasena()));
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

    @Transactional
    public RespuestaLogin iniciarSesion(SolicitudLogin solicitud) {
        Usuario usuario = usuarioRepository.buscarPorCorreo(solicitud.correo())
                .orElseThrow(() -> {
                    auditarEvento("LOGIN_FALLIDO", null, ResultadoAuditoria.ERROR, "Correo inexistente", Map.of("correo", solicitud.correo()));
                    return new RecursoNoEncontradoException("El correo no existe");
                });

        if (!usuario.isActivo()) {
            auditarEvento("LOGIN_FALLIDO", usuario, ResultadoAuditoria.ERROR, "Usuario inactivo", Map.of("correo", usuario.getCorreo()));
            throw new ExcepcionNegocio("El usuario no se encuentra habilitado");
        }

        boolean contrasenaValida = passwordEncoder.matches(solicitud.contrasena(), usuario.getContrasenaHash());
        if (!contrasenaValida) {
            auditarEvento("LOGIN_FALLIDO", usuario, ResultadoAuditoria.ERROR, "Credenciales invalidas", Map.of("correo", usuario.getCorreo()));
            throw new ExcepcionNegocio("Credenciales invalidas");
        }

        String tokenAcceso = servicioJwt.generarTokenAcceso(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getRol().name()
        );
        String refreshTokenPlano = generarRefreshTokenPlano();
        registrarRefreshToken(usuario.getId(), refreshTokenPlano, null);
        auditarEvento("LOGIN_CORRECTO", usuario, ResultadoAuditoria.OK, "Inicio de sesion correcto", Map.of("correo", usuario.getCorreo()));

        return new RespuestaLogin(
                true,
                "Inicio de sesion correcto",
                usuario.getId(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getRol().name(),
                tokenAcceso,
                refreshTokenPlano,
                "Bearer"
        );
    }

    @Transactional
    public RespuestaRenovarToken renovarToken(SolicitudRenovarToken solicitud) {
        RefreshToken refreshActual;
        try {
            refreshActual = obtenerRefreshTokenActivo(solicitud.refreshToken());
        } catch (RuntimeException ex) {
            auditarEvento("REFRESH_TOKEN_FALLIDO", null, ResultadoAuditoria.ERROR, ex.getMessage(), Map.of("motivo", "token_invalido"));
            throw ex;
        }
        validarRefreshVigente(refreshActual);

        Usuario usuario = usuarioRepository.findById(refreshActual.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        if (!usuario.isActivo()) {
            auditarEvento("REFRESH_TOKEN_FALLIDO", usuario, ResultadoAuditoria.ERROR, "Usuario inactivo", Map.of("usuarioId", usuario.getId()));
            throw new ExcepcionNegocio("El usuario no se encuentra habilitado");
        }

        revocarRefreshToken(refreshActual, usuario.getId());

        String tokenAcceso = servicioJwt.generarTokenAcceso(
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getRol().name()
        );
        String refreshNuevoPlano = generarRefreshTokenPlano();
        registrarRefreshToken(usuario.getId(), refreshNuevoPlano, usuario.getId());
        auditarEvento("REFRESH_TOKEN_ROTADO", usuario, ResultadoAuditoria.OK, "Refresh token rotado correctamente", Map.of("usuarioId", usuario.getId()));

        return new RespuestaRenovarToken(
                true,
                "Token renovado correctamente",
                tokenAcceso,
                refreshNuevoPlano,
                "Bearer"
        );
    }

    @Transactional
    public RespuestaBasica cerrarSesion(SolicitudCerrarSesion solicitud) {
        RefreshToken refreshActual = obtenerRefreshTokenActivo(solicitud.refreshToken());
        revocarRefreshToken(refreshActual, refreshActual.getUsuarioId());
        usuarioRepository.findById(refreshActual.getUsuarioId())
                .ifPresent(usuario -> auditarEvento("LOGOUT", usuario, ResultadoAuditoria.OK, "Sesion cerrada correctamente", Map.of("usuarioId", usuario.getId())));
        return new RespuestaBasica(true, "Sesion cerrada correctamente");
    }

    private RefreshToken obtenerRefreshTokenActivo(String refreshTokenPlano) {
        String hash = hashTextoUtil.hashearSha256(refreshTokenPlano);
        return refreshTokenRepository
                .findTopByTokenHashAndActivoTrueAndRevocadoFalseOrderByFechaCreacionDesc(hash)
                .orElseThrow(() -> new ExcepcionNegocio("Refresh token invalido o revocado"));
    }

    private void validarRefreshVigente(RefreshToken refreshToken) {
        if (refreshToken.getExpiraEn().isBefore(OffsetDateTime.now())) {
            revocarRefreshToken(refreshToken, null);
            throw new ExcepcionNegocio("Refresh token expirado");
        }
    }

    private void registrarRefreshToken(String usuarioId, String refreshTokenPlano, String revocadoPorUsuarioId) {
        if (revocadoPorUsuarioId != null) {
            invalidarRefreshActivos(usuarioId, revocadoPorUsuarioId);
        }

        RefreshToken token = new RefreshToken();
        token.setId(UUID.randomUUID().toString());
        token.setUsuarioId(usuarioId);
        token.setTokenHash(hashTextoUtil.hashearSha256(refreshTokenPlano));
        token.setExpiraEn(OffsetDateTime.now().plusDays(configuracionSeguridadService.obtenerRefreshExpiracionDias()));
        token.setRevocado(false);
        token.setActivo(true);
        token.setFechaCreacion(OffsetDateTime.now());
        refreshTokenRepository.save(token);
    }

    private void invalidarRefreshActivos(String usuarioId, String revocadoPorUsuarioId) {
        List<RefreshToken> activos = refreshTokenRepository.findByUsuarioIdAndActivoTrueAndRevocadoFalse(usuarioId);
        for (RefreshToken token : activos) {
            revocarRefreshToken(token, revocadoPorUsuarioId);
        }
    }

    private void revocarRefreshToken(RefreshToken refreshToken, String revocadoPorUsuarioId) {
        refreshToken.setRevocado(true);
        refreshToken.setActivo(false);
        refreshToken.setRevocadoPor(revocadoPorUsuarioId);
        refreshToken.setFechaRevocacion(OffsetDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }

    private String generarDniTemporalUnico() {
        String dni;
        do {
            int numero = ThreadLocalRandom.current().nextInt(10_000_000, 99_999_999);
            dni = String.valueOf(numero);
        } while (usuarioRepository.existsByDni(dni));
        return dni;
    }

    private String generarRefreshTokenPlano() {
        byte[] bytes = new byte[48];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private void auditarEvento(String accion, Usuario usuario, ResultadoAuditoria resultado, String detalle, Map<String, Object> datos) {
        auditoriaService.registrarAccion(new RegistroAuditoriaInterna(
                "AUTH",
                usuario == null ? null : usuario.getId(),
                usuario == null ? null : usuario.getRol(),
                accion,
                usuario == null ? null : usuario.getId(),
                resultado,
                detalle,
                null,
                datos,
                null,
                null,
                UUID.randomUUID().toString()
        ));
    }
}
