package com.utp.safezonebackend.casos.mapper;

import com.utp.safezonebackend.casos.dto.response.CasoResponse;
import com.utp.safezonebackend.casos.entity.Caso;
import org.springframework.stereotype.Component;

@Component
public class CasoMapper {

    public CasoResponse toResponse(Caso entity) {
        return new CasoResponse();
    }
}
