package com.utp.safezonebackend.seguimientos.mapper;

import com.utp.safezonebackend.seguimientos.dto.response.SeguimientoCasoResponse;
import com.utp.safezonebackend.seguimientos.entity.SeguimientoCaso;
import org.springframework.stereotype.Component;

@Component
public class SeguimientoCasoMapper {

    public SeguimientoCasoResponse toResponse(SeguimientoCaso entity) {
        return new SeguimientoCasoResponse();
    }
}
