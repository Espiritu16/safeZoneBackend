package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.UsuarioResponse;
import com.utp.safezonebackend.persistance.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(Usuario entity) {
        return new UsuarioResponse();
    }
}
