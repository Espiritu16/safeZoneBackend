package com.utp.safezonebackend.usuarios.mapper;

import com.utp.safezonebackend.usuarios.dto.response.UsuarioResponse;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario entity) {
        return new UsuarioResponse(
                entity.getId(),
                entity.getCorreo(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getDni(),
                entity.getTelefono(),
                entity.getDistrito(),
                entity.getRol() == null ? null : entity.getRol().name(),
                entity.isActivo(),
                entity.getFechaCreacion(),
                entity.getFechaActualizacion(),
                entity.getCreadoPor(),
                entity.getActualizadoPor(),
                entity.getInactivadoPor(),
                entity.getFechaInactivacion()
        );
    }
}
