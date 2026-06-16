package com.utp.safezonebackend.seguimientos.mapper;

import com.utp.safezonebackend.seguimientos.dto.response.SeguimientoCasoResponse;
import com.utp.safezonebackend.seguimientos.entity.SeguimientoCaso;
import org.springframework.stereotype.Component;

@Component
public class SeguimientoCasoMapper {

    public SeguimientoCasoResponse toResponse(SeguimientoCaso entity) {
        if (entity == null) {
            return null;
        }
        return new SeguimientoCasoResponse(
                entity.getId(),
                entity.getCasoId(),
                entity.getAutorId(),
                entity.getRolAutor(),
                entity.getTipoSeguimiento(),
                entity.getContenido(),
                entity.getProximaAccion(),
                entity.getFechaProximaAccion(),
                entity.isActivo(),
                entity.getFechaCreacion(),
                entity.getFechaActualizacion()
        );
    }
}
