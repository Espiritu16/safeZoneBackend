package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.SeguimientoCasoResponse;
import com.utp.safezonebackend.persistance.entity.SeguimientoCaso;
import org.springframework.stereotype.Component;

@Component
public class SeguimientoCasoMapper {

    public SeguimientoCasoResponse toResponse(SeguimientoCaso entity) {
        return new SeguimientoCasoResponse();
    }
}
