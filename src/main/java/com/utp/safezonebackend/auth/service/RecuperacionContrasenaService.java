package com.utp.safezonebackend.auth.service;

import com.utp.safezonebackend.auditoria.dto.request.RegistroAuditoriaInterna;
import com.utp.safezonebackend.auditoria.enums.ResultadoAuditoria;
import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.auth.dto.request.SolicitudRecuperarContrasena;
import com.utp.safezonebackend.auth.dto.request.SolicitudRestablecerContrasena;
import com.utp.safezonebackend.auth.dto.request.SolicitudVerificarCodigo;
import com.utp.safezonebackend.auth.dto.response.RespuestaBasica;
import com.utp.safezonebackend.auth.entity.RecuperacionContrasenaCodigo;
import com.utp.safezonebackend.auth.repository.RecuperacionContrasenaCodigoRepository;
import com.utp.safezonebackend.configuracion.service.ConfiguracionSeguridadService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.shared.util.GeneradorCodigoRecuperacion;
import com.utp.safezonebackend.shared.util.HashTextoUtil;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecuperacionContrasenaService {

    private final UsuarioRepository usuarioRepository;
    private final RecuperacionContrasenaCodigoRepository codigoRepository;
    private final GeneradorCodigoRecuperacion generadorCodigoRecuperacion;
    private final HashTextoUtil hashTextoUtil;
    private final CorreoService correoService;
    private final PasswordEncoder passwordEncoder;
    private final ConfiguracionSeguridadService configuracionSeguridadService;
    private final AuditoriaService auditoriaService;

    public RecuperacionContrasenaService(
            UsuarioRepository usuarioRepository,
            RecuperacionContrasenaCodigoRepository codigoRepository,
            GeneradorCodigoRecuperacion generadorCodigoRecuperacion,
            HashTextoUtil hashTextoUtil,
            CorreoService correoService,
            PasswordEncoder passwordEncoder,
            ConfiguracionSeguridadService configuracionSeguridadService,
            AuditoriaService auditoriaService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.codigoRepository = codigoRepository;
        this.generadorCodigoRecuperacion = generadorCodigoRecuperacion;
        this.hashTextoUtil = hashTextoUtil;
        this.correoService = correoService;
        this.passwordEncoder = passwordEncoder;
        this.configuracionSeguridadService = configuracionSeguridadService;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public RespuestaBasica solicitarCodigoRecuperacion(SolicitudRecuperarContrasena solicitud) {
        Usuario usuario = obtenerUsuarioPorCorreo(solicitud.correo());
        invalidarCodigosActivos(usuario.getId());

        String codigoPlano = generadorCodigoRecuperacion.generarCodigo();
        String codigoHash = hashTextoUtil.hashearSha256(codigoPlano);

        RecuperacionContrasenaCodigo codigo = new RecuperacionContrasenaCodigo();
        codigo.setId(UUID.randomUUID().toString());
        codigo.setUsuarioId(usuario.getId());
        codigo.setCodigoHash(codigoHash);
        codigo.setExpiraEn(LocalDateTime.now().plusMinutes(configuracionSeguridadService.obtenerRecuperacionExpiracionMinutos()));
        codigo.setUsado(false);
        codigo.setIntentos(0);
        codigo.setMaxIntentos(configuracionSeguridadService.obtenerRecuperacionMaxIntentos());
        codigo.setActivo(true);
        codigo.setFechaCreacion(LocalDateTime.now());
        codigoRepository.save(codigo);

        correoService.enviarCodigoRecuperacion(usuario.getCorreo(), codigoPlano);
        auditarRecuperacion("RECUPERACION_CODIGO_SOLICITADO", usuario, ResultadoAuditoria.OK, "Codigo de recuperacion solicitado");
        return new RespuestaBasica(true, "Codigo enviado correctamente");
    }

    @Transactional(noRollbackFor = ExcepcionNegocio.class)
    public RespuestaBasica verificarCodigoRecuperacion(SolicitudVerificarCodigo solicitud) {
        Usuario usuario = obtenerUsuarioPorCorreo(solicitud.correo());
        RecuperacionContrasenaCodigo codigo = obtenerCodigoActivoPorUsuario(usuario.getId());

        validarCodigoNoExpirado(codigo);
        validarIntentos(codigo);

        String codigoHashSolicitud = hashTextoUtil.hashearSha256(solicitud.codigo());
        if (!codigoHashSolicitud.equals(codigo.getCodigoHash().trim())) {
            codigo.setIntentos(codigo.getIntentos() + 1);
            codigoRepository.save(codigo);
            auditarRecuperacion("RECUPERACION_CODIGO_FALLIDO", usuario, ResultadoAuditoria.ERROR, "Codigo de recuperacion incorrecto");
            throw new ExcepcionNegocio("El codigo es invalido o ha expirado");
        }

        auditarRecuperacion("RECUPERACION_CODIGO_VERIFICADO", usuario, ResultadoAuditoria.OK, "Codigo de recuperacion verificado");
        return new RespuestaBasica(true, "Codigo verificado correctamente");
    }

    @Transactional(noRollbackFor = ExcepcionNegocio.class)
    public RespuestaBasica restablecerContrasena(SolicitudRestablecerContrasena solicitud) {
        Usuario usuario = obtenerUsuarioPorCorreo(solicitud.correo());
        RecuperacionContrasenaCodigo codigo = obtenerCodigoActivoPorUsuario(usuario.getId());

        validarCodigoNoExpirado(codigo);
        validarIntentos(codigo);

        String codigoHashSolicitud = hashTextoUtil.hashearSha256(solicitud.codigo());
        if (!codigoHashSolicitud.equals(codigo.getCodigoHash().trim())) {
            codigo.setIntentos(codigo.getIntentos() + 1);
            codigoRepository.save(codigo);
            auditarRecuperacion("RECUPERACION_RESTABLECER_FALLIDO", usuario, ResultadoAuditoria.ERROR, "Codigo de recuperacion incorrecto");
            throw new ExcepcionNegocio("El codigo es invalido o ha expirado");
        }

        configuracionSeguridadService.validarContrasenaSegura(solicitud.nuevaPassword());
        usuario.setContrasenaHash(passwordEncoder.encode(solicitud.nuevaPassword()));
        usuario.setFechaActualizacion(OffsetDateTime.now());
        usuarioRepository.save(usuario);

        codigo.setUsado(true);
        codigo.setActivo(false);
        codigo.setFechaUso(LocalDateTime.now());
        codigoRepository.save(codigo);

        auditarRecuperacion("RECUPERACION_CONTRASENA_RESTABLECIDA", usuario, ResultadoAuditoria.OK, "Contrasena restablecida correctamente");
        return new RespuestaBasica(true, "Contrasena restablecida correctamente");
    }

    private Usuario obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.buscarPorCorreo(correo)
                .orElseThrow(() -> new RecursoNoEncontradoException("El correo no existe"));
    }

    private RecuperacionContrasenaCodigo obtenerCodigoActivoPorUsuario(String usuarioId) {
        return codigoRepository.findTopByUsuarioIdAndActivoTrueAndUsadoFalseOrderByFechaCreacionDesc(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un codigo activo para este correo"));
    }

    private void invalidarCodigosActivos(String usuarioId) {
        List<RecuperacionContrasenaCodigo> codigosActivos = codigoRepository.findByUsuarioIdAndActivoTrueAndUsadoFalse(usuarioId);
        for (RecuperacionContrasenaCodigo codigo : codigosActivos) {
            codigo.setActivo(false);
            codigoRepository.save(codigo);
        }
    }

    private void validarCodigoNoExpirado(RecuperacionContrasenaCodigo codigo) {
        if (codigo.getExpiraEn().isBefore(LocalDateTime.now())) {
            codigo.setActivo(false);
            codigoRepository.save(codigo);
            usuarioRepository.findById(codigo.getUsuarioId())
                    .ifPresent(usuario -> auditarRecuperacion("RECUPERACION_CODIGO_EXPIRADO", usuario, ResultadoAuditoria.ERROR, "Codigo de recuperacion expirado"));
            throw new ExcepcionNegocio("El codigo es invalido o ha expirado");
        }
    }

    private void validarIntentos(RecuperacionContrasenaCodigo codigo) {
        if (codigo.getIntentos() >= codigo.getMaxIntentos()) {
            codigo.setActivo(false);
            codigoRepository.save(codigo);
            usuarioRepository.findById(codigo.getUsuarioId())
                    .ifPresent(usuario -> auditarRecuperacion("RECUPERACION_INTENTOS_SUPERADOS", usuario, ResultadoAuditoria.ERROR, "Se supero el numero maximo de intentos"));
            throw new ExcepcionNegocio("Se supero el numero maximo de intentos");
        }
    }

    private void auditarRecuperacion(String accion, Usuario usuario, ResultadoAuditoria resultado, String detalle) {
        auditoriaService.registrarAccion(new RegistroAuditoriaInterna(
                "RECUPERACION_CONTRASENA",
                usuario.getId(),
                usuario.getRol(),
                accion,
                usuario.getId(),
                resultado,
                detalle,
                null,
                Map.of("correo", usuario.getCorreo()),
                null,
                null,
                UUID.randomUUID().toString()
        ));
    }
}
