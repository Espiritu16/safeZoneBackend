package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.persistance.entity.Evidencia;
import org.springframework.stereotype.Component;

@Component
public class EvidenciaMapper {

    public EvidenciaResponse toResponse(Evidencia entity) {
        return new EvidenciaResponse();
    }
}
