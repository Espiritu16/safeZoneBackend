package com.utp.safezonebackend.auditoria.aspect;

import com.utp.safezonebackend.auditoria.dto.request.RegistroAuditoriaInterna;
import com.utp.safezonebackend.auditoria.enums.ResultadoAuditoria;
import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditoriaAccionesAspect {

    private final AuditoriaService auditoriaService;
    private final UsuarioRepository usuarioRepository;
    private final HttpServletRequest request;

    public AuditoriaAccionesAspect(
            AuditoriaService auditoriaService,
            UsuarioRepository usuarioRepository,
            HttpServletRequest request
    ) {
        this.auditoriaService = auditoriaService;
        this.usuarioRepository = usuarioRepository;
        this.request = request;
    }

    @Around("within(com.utp.safezonebackend..controller..*)")
    public Object auditarOperacion(ProceedingJoinPoint joinPoint) throws Throwable {
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/auditoria")) {
            return joinPoint.proceed();
        }

        String metodoHttp = request.getMethod();
        if (!debeAuditar(metodoHttp, uri)) {
            return joinPoint.proceed();
        }

        Usuario actor = resolverActorActual();
        String entidadTipo = obtenerEntidadTipo(uri);
        String entidadId = obtenerEntidadId(uri);
        String accion = resolverAccion(metodoHttp, uri);
        String requestId = UUID.randomUUID().toString();
        Map<String, Object> antes = construirContextoBase(joinPoint);

        try {
            Object resultado = joinPoint.proceed();
            Map<String, Object> despues = construirResultado(resultado);
            auditoriaService.registrarAccion(new RegistroAuditoriaInterna(
                    entidadTipo,
                    actor == null ? null : actor.getId(),
                    actor == null ? null : actor.getRol(),
                    accion,
                    entidadId,
                    ResultadoAuditoria.OK,
                    "Operacion ejecutada correctamente",
                    antes,
                    despues,
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent"),
                    requestId
            ));
            return resultado;
        } catch (Throwable ex) {
            Map<String, Object> despues = new HashMap<>();
            despues.put("error", ex.getClass().getSimpleName());
            despues.put("mensaje", ex.getMessage());
            auditoriaService.registrarAccion(new RegistroAuditoriaInterna(
                    entidadTipo,
                    actor == null ? null : actor.getId(),
                    actor == null ? null : actor.getRol(),
                    accion,
                    entidadId,
                    ResultadoAuditoria.ERROR,
                    "Operacion fallida",
                    antes,
                    despues,
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent"),
                    requestId
            ));
            throw ex;
        }
    }

    private boolean debeAuditar(String metodoHttp, String uri) {
        if ("POST".equalsIgnoreCase(metodoHttp)
                || "PUT".equalsIgnoreCase(metodoHttp)
                || "PATCH".equalsIgnoreCase(metodoHttp)
                || "DELETE".equalsIgnoreCase(metodoHttp)) {
            return true;
        }
        if ("GET".equalsIgnoreCase(metodoHttp)) {
            return uri.startsWith("/api/casos")
                    || uri.startsWith("/api/denuncias")
                    || uri.startsWith("/api/evidencias")
                    || uri.startsWith("/api/citas")
                    || uri.startsWith("/api/seguimientos")
                    || uri.startsWith("/api/victimasalias");
        }
        return false;
    }

    private String resolverAccion(String metodoHttp, String uri) {
        if (uri.startsWith("/api/auth/iniciar-sesion")) {
            return "INICIO_SESION";
        }
        if (uri.startsWith("/api/auth/recuperar-contrasena")
                || uri.startsWith("/api/auth/verificar-codigo")
                || uri.startsWith("/api/auth/restablecer-contrasena")) {
            return "RECUPERACION_CONTRASENA";
        }
        if ("GET".equalsIgnoreCase(metodoHttp)) {
            return "ACCESO_EXPEDIENTE";
        }
        if ("POST".equalsIgnoreCase(metodoHttp)) {
            return "CREACION";
        }
        if ("DELETE".equalsIgnoreCase(metodoHttp)) {
            return "ELIMINACION_LOGICA";
        }
        if (uri.toLowerCase().contains("estado")) {
            return "CAMBIO_ESTADO";
        }
        return "MODIFICACION";
    }

    private String obtenerEntidadTipo(String uri) {
        String[] partes = uri.split("/");
        if (partes.length < 3) {
            return "SISTEMA";
        }
        return partes[2].toUpperCase();
    }

    private String obtenerEntidadId(String uri) {
        String[] partes = uri.split("/");
        if (partes.length >= 4) {
            return partes[3];
        }
        return null;
    }

    private Usuario resolverActorActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        Optional<Usuario> usuario = usuarioRepository.buscarPorCorreo(auth.getName());
        return usuario.orElse(null);
    }

    private Map<String, Object> construirContextoBase(ProceedingJoinPoint joinPoint) {
        Map<String, Object> datos = new HashMap<>();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        datos.put("metodo", methodSignature.getDeclaringType().getSimpleName() + "." + methodSignature.getName());
        datos.put("uri", request.getRequestURI());
        datos.put("metodoHttp", request.getMethod());
        datos.put("query", request.getQueryString());
        return datos;
    }

    private Map<String, Object> construirResultado(Object resultado) {
        Map<String, Object> datos = new HashMap<>();
        if (resultado instanceof ResponseEntity<?> responseEntity) {
            datos.put("httpStatus", responseEntity.getStatusCode().value());
        } else {
            datos.put("resultado", resultado == null ? "null" : resultado.getClass().getSimpleName());
        }
        return datos;
    }
}
