package com.utp.safezonebackend.asignaciones.mapper;

import com.utp.safezonebackend.asignaciones.dto.response.AsignacionCasoResponse;
import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import org.springframework.stereotype.Component;

@Component
public class AsignacionCasoMapper {

    public AsignacionCasoResponse toResponse(AsignacionCaso entity) {
        return new AsignacionCasoResponse();
    }
}
