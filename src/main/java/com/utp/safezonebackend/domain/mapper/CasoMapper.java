package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.CasoResponse;
import com.utp.safezonebackend.persistance.entity.Caso;
import org.springframework.stereotype.Component;

@Component
public class CasoMapper {

    public CasoResponse toResponse(Caso entity) {
        return new CasoResponse();
    }
}
