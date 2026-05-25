package com.utp.safezonebackend.usuarios.mapper;

import com.utp.safezonebackend.usuarios.dto.response.UsuarioResponse;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario entity) {
        return new UsuarioResponse();
    }
}
