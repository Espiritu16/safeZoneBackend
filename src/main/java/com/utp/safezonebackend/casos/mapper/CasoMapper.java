package com.utp.safezonebackend.casos.mapper;

import com.utp.safezonebackend.casos.dto.response.CasoResponse;
import com.utp.safezonebackend.casos.entity.Caso;
import org.springframework.stereotype.Component;

@Component
public class CasoMapper {

    public CasoResponse toResponse(Caso entity) {
        if (entity == null) {
            return null;
        }
        return new CasoResponse(
                entity.getId(),
                entity.getVictimaId(),
                entity.getEstado(),
                entity.getPrioridad(),
                entity.getResumen(),
                entity.getDistrito(),
                entity.isActivo(),
                entity.getFechaCreacion(),
                entity.getFechaCierre(),
                entity.getFechaActualizacion()
        );
    }
}
