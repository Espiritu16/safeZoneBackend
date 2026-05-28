package com.utp.safezonebackend.evidencias.mapper;

import com.utp.safezonebackend.evidencias.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.evidencias.entity.Evidencia;
import org.springframework.stereotype.Component;

@Component
public class EvidenciaMapper {

    public EvidenciaResponse toResponse(Evidencia entity) {
        return new EvidenciaResponse();
    }
}
