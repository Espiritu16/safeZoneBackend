package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.AsignacionCasoResponse;
import com.utp.safezonebackend.persistance.entity.AsignacionCaso;
import org.springframework.stereotype.Component;

@Component
public class AsignacionCasoMapper {

    public AsignacionCasoResponse toResponse(AsignacionCaso entity) {
        return new AsignacionCasoResponse();
    }
}
