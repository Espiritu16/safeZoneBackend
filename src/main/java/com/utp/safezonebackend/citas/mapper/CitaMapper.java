package com.utp.safezonebackend.citas.mapper;

import com.utp.safezonebackend.citas.dto.response.CitaResponse;
import com.utp.safezonebackend.citas.entity.Cita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public CitaResponse toResponse(Cita entity) {
        return new CitaResponse(
                entity.getId(),
                entity.getCasoId(),
                entity.getVictimaId(),
                entity.getEspecialistaId(),
                entity.getTipoCita(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getEstado(),
                entity.getMotivoCancelacion(),
                entity.getObservaciones(),
                entity.isActivo(),
                entity.getFechaCreacion(),
                entity.getFechaActualizacion()
        );
    }
}
