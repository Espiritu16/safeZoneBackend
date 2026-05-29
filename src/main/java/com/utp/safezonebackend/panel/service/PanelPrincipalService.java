package com.utp.safezonebackend.panel.service;

import com.utp.safezonebackend.panel.dto.response.PanelRolResponse;
import com.utp.safezonebackend.panel.dto.response.PanelRolResponse.AccionRapida;
import com.utp.safezonebackend.panel.dto.response.PanelRolResponse.Alerta;
import com.utp.safezonebackend.panel.dto.response.PanelRolResponse.Indicador;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PanelPrincipalService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UsuarioRepository usuarioRepository;

    public PanelPrincipalService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public PanelRolResponse obtenerPanelActual() {
        Usuario usuario = obtenerUsuarioAutenticado();
        RolUsuario rol = usuario.getRol();
        return new PanelRolResponse(
                usuario.getId(),
                usuario.getNombres(),
                rol.name(),
                modulos(rol),
                permisos(rol),
                indicadores(usuario),
                acciones(rol),
                alertas(rol)
        );
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            throw new ExcepcionNegocio("Usuario no autenticado");
        }
        return usuarioRepository.buscarPorCorreo(auth.getName())
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new ExcepcionNegocio("Usuario no autenticado"));
    }

    private List<Indicador> indicadores(Usuario usuario) {
        RolUsuario rol = usuario.getRol();
        return switch (rol) {
            case ADMIN -> List.of(
                    indicador("usuarios_activos", "Usuarios activos", contar("SELECT COUNT(*) FROM usuario WHERE activo = 1")),
                    indicador("casos_abiertos", "Casos abiertos", contar("SELECT COUNT(*) FROM caso WHERE activo = 1 AND estado <> 'CERRADO'")),
                    indicador("denuncias_registradas", "Denuncias registradas", contar("SELECT COUNT(*) FROM denuncia WHERE activo = 1")),
                    indicador("eventos_auditoria", "Eventos de auditoria", contar("SELECT COUNT(*) FROM auditoria"))
            );
            case RECEPCIONISTA -> List.of(
                    indicador("predenuncias_pendientes", "Predenuncias pendientes", contar("SELECT COUNT(*) FROM pre_denuncia WHERE activo = 1 AND estado = 'PENDIENTE'")),
                    indicador("predenuncias_en_contacto", "Predenuncias en contacto", contar("SELECT COUNT(*) FROM pre_denuncia WHERE activo = 1 AND estado = 'EN_CONTACTO'")),
                    indicador("denuncias_formalizadas", "Denuncias formalizadas", contar("SELECT COUNT(*) FROM denuncia WHERE activo = 1")),
                    indicador("casos_abiertos", "Casos abiertos", contar("SELECT COUNT(*) FROM caso WHERE activo = 1 AND estado <> 'CERRADO'"))
            );
            case PSICOLOGO, DEFENSOR -> List.of(
                    indicador("casos_asignados", "Casos asignados", contar("SELECT COUNT(*) FROM asignacion_caso WHERE activo = 1 AND profesional_id = :usuarioId", usuario.getId())),
                    indicador("seguimientos_registrados", "Seguimientos registrados", contar("SELECT COUNT(*) FROM seguimiento_caso WHERE activo = 1 AND autor_id = :usuarioId", usuario.getId())),
                    indicador("citas_programadas", "Citas programadas", contar("SELECT COUNT(*) FROM cita WHERE activo = 1 AND especialista_id = :usuarioId AND estado IN ('PROGRAMADA','CONFIRMADA')", usuario.getId())),
                    indicador("evidencias_subidas", "Evidencias subidas", contar("SELECT COUNT(*) FROM evidencia WHERE activo = 1 AND subido_por = :usuarioId", usuario.getId()))
            );
            case VICTIMA -> List.of(
                    indicador("mis_predenuncias", "Mis predenuncias", contar("SELECT COUNT(*) FROM pre_denuncia WHERE activo = 1 AND victima_id = :usuarioId", usuario.getId())),
                    indicador("mis_denuncias", "Mis denuncias", contar("SELECT COUNT(*) FROM denuncia WHERE activo = 1 AND victima_id = :usuarioId", usuario.getId())),
                    indicador("mis_casos", "Mis casos", contar("SELECT COUNT(*) FROM caso WHERE activo = 1 AND victima_id = :usuarioId", usuario.getId())),
                    indicador("mis_citas", "Mis citas", contar("SELECT COUNT(*) FROM cita WHERE activo = 1 AND victima_id = :usuarioId", usuario.getId()))
            );
        };
    }

    private List<AccionRapida> acciones(RolUsuario rol) {
        return switch (rol) {
            case ADMIN -> List.of(
                    accion("gestionar_usuarios", "Gestionar usuarios", "usuarios"),
                    accion("ver_auditoria", "Ver auditoria", "auditoria"),
                    accion("configurar_seguridad", "Configurar seguridad", "configuracion")
            );
            case RECEPCIONISTA -> List.of(
                    accion("revisar_predenuncias", "Revisar predenuncias", "predenuncias"),
                    accion("registrar_victima", "Registrar victima", "victimas"),
                    accion("formalizar_denuncia", "Formalizar denuncia", "denuncias")
            );
            case PSICOLOGO, DEFENSOR -> List.of(
                    accion("ver_casos_asignados", "Ver casos asignados", "casos_asignados"),
                    accion("registrar_seguimiento", "Registrar seguimiento", "seguimientos"),
                    accion("gestionar_citas", "Gestionar citas", "citas")
            );
            case VICTIMA -> List.of(
                    accion("ver_historial", "Ver historial", "mis_casos"),
                    accion("ver_citas", "Ver citas", "mis_citas"),
                    accion("ver_notificaciones", "Ver notificaciones", "notificaciones")
            );
        };
    }

    private List<Alerta> alertas(RolUsuario rol) {
        if (rol == RolUsuario.RECEPCIONISTA && contar("SELECT COUNT(*) FROM pre_denuncia WHERE activo = 1 AND estado = 'PENDIENTE'") > 0) {
            return List.of(new Alerta("PREDENUNCIAS", "Hay predenuncias pendientes de contacto.", "MEDIA"));
        }
        if (rol == RolUsuario.ADMIN && contar("SELECT COUNT(*) FROM auditoria WHERE resultado = 'ERROR'") > 0) {
            return List.of(new Alerta("AUDITORIA", "Existen eventos fallidos registrados.", "ALTA"));
        }
        return List.of();
    }

    private List<String> modulos(RolUsuario rol) {
        return switch (rol) {
            case ADMIN -> List.of("usuarios", "auditoria", "configuracion", "reportes", "casos");
            case RECEPCIONISTA -> List.of("predenuncias", "victimas", "denuncias", "casos", "citas");
            case PSICOLOGO, DEFENSOR -> List.of("casos_asignados", "seguimientos", "citas", "evidencias");
            case VICTIMA -> List.of("mis_denuncias", "mis_casos", "mis_citas", "notificaciones");
        };
    }

    private List<String> permisos(RolUsuario rol) {
        return switch (rol) {
            case ADMIN -> List.of("usuarios:administrar", "auditoria:leer", "configuracion:administrar", "reportes:leer");
            case RECEPCIONISTA -> List.of("predenuncias:gestionar", "victimas:registrar", "denuncias:formalizar", "citas:programar");
            case PSICOLOGO -> List.of("casos_asignados:leer", "seguimientos:registrar", "citas:gestionar", "evidencias:leer");
            case DEFENSOR -> List.of("casos_asignados:leer", "seguimientos:registrar", "citas:gestionar", "evidencias:gestionar");
            case VICTIMA -> List.of("mis_denuncias:leer", "mis_casos:leer", "mis_citas:leer", "notificaciones:leer");
        };
    }

    private Indicador indicador(String clave, String etiqueta, long valor) {
        return new Indicador(clave, etiqueta, valor);
    }

    private AccionRapida accion(String clave, String etiqueta, String modulo) {
        return new AccionRapida(clave, etiqueta, modulo);
    }

    private long contar(String sql) {
        return ((Number) entityManager.createNativeQuery(sql).getSingleResult()).longValue();
    }

    private long contar(String sql, String usuarioId) {
        return ((Number) entityManager.createNativeQuery(sql)
                .setParameter("usuarioId", usuarioId)
                .getSingleResult()).longValue();
    }
}
