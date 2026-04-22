package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.DenunciaResponse;
import com.utp.safezonebackend.persistance.entity.Denuncia;
import org.springframework.stereotype.Component;

@Component
public class DenunciaMapper {

    public DenunciaResponse toResponse(Denuncia entity) {
        return new DenunciaResponse();
    }
}
