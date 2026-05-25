package com.utp.safezonebackend.auth.mapper;

import com.utp.safezonebackend.auth.dto.response.RefreshTokenResponse;
import com.utp.safezonebackend.auth.entity.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    public RefreshTokenResponse toResponse(RefreshToken entity) {
        return new RefreshTokenResponse(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getExpiraEn(),
                entity.isRevocado(),
                entity.getRevocadoPor(),
                entity.getFechaRevocacion(),
                entity.getFechaCreacion(),
                entity.isActivo()
        );
    }
}
