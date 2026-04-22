package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.RefreshTokenResponse;
import com.utp.safezonebackend.persistance.entity.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    public RefreshTokenResponse toResponse(RefreshToken entity) {
        return new RefreshTokenResponse();
    }
}
