package com.utp.safezonebackend.auth.service;

import com.utp.safezonebackend.auth.dto.response.RespuestaContextoSesion;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContextoSesionService {

    private final UsuarioRepository usuarioRepository;

    public ContextoSesionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public RespuestaContextoSesion obtenerContextoActual() {
        Usuario usuario = obtenerUsuarioAutenticado();
        return new RespuestaContextoSesion(
                true,
                "Contexto de sesion obtenido correctamente",
                usuario.getId(),
                usuario.getNombres(),
                usuario.getCorreo(),
                usuario.getRol().name(),
                obtenerPermisos(usuario.getRol()),
                obtenerModulos(usuario.getRol())
        );
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            throw new ExcepcionNegocio("Usuario no autenticado");
        }
        Usuario usuario = usuarioRepository.buscarPorCorreo(auth.getName())
                .orElseThrow(() -> new ExcepcionNegocio("Usuario no autenticado"));
        if (!usuario.isActivo()) {
            throw new ExcepcionNegocio("El usuario no se encuentra habilitado");
        }
        return usuario;
    }

    private List<String> obtenerModulos(RolUsuario rol) {
        return switch (rol) {
            case ADMIN -> List.of("usuarios", "auditoria", "configuracion", "reportes", "casos");
            case RECEPCIONISTA -> List.of("predenuncias", "victimas", "denuncias", "casos", "citas");
            case PSICOLOGO, DEFENSOR -> List.of("casos_asignados", "seguimientos", "citas", "evidencias");
            case VICTIMA -> List.of("mis_denuncias", "mis_casos", "mis_citas", "notificaciones");
            case SOPORTE -> List.of("auditoria_limitada", "notificaciones", "soporte_operativo");
        };
    }

    private List<String> obtenerPermisos(RolUsuario rol) {
        return switch (rol) {
            case ADMIN -> List.of(
                    "usuarios:leer", "usuarios:crear", "usuarios:actualizar", "usuarios:inactivar",
                    "roles:administrar", "auditoria:leer", "configuracion:administrar", "reportes:leer", "casos:leer"
            );
            case RECEPCIONISTA -> List.of(
                    "predenuncias:gestionar", "victimas:registrar", "denuncias:formalizar", "casos:crear", "citas:programar"
            );
            case PSICOLOGO -> List.of("casos_asignados:leer", "seguimientos:registrar", "citas:gestionar", "evidencias:leer");
            case DEFENSOR -> List.of("casos_asignados:leer", "seguimientos:registrar", "citas:gestionar", "evidencias:gestionar");
            case VICTIMA -> List.of("mis_denuncias:leer", "mis_casos:leer", "mis_citas:leer", "notificaciones:leer");
            case SOPORTE -> List.of("auditoria_limitada:leer", "notificaciones:gestionar", "soporte_operativo:gestionar");
        };
    }
}
