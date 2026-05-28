package com.utp.safezonebackend.auditoria.mapper;

import com.utp.safezonebackend.auditoria.dto.response.AuditoriaResponse;
import com.utp.safezonebackend.auditoria.entity.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaResponse toResponse(Auditoria entity) {
        return new AuditoriaResponse(
                entity.getId(),
                entity.getEntidadTipo(),
                entity.getFecha(),
                entity.getActorId(),
                entity.getActorRol() == null ? null : entity.getActorRol().name(),
                entity.getAccion(),
                entity.getEntidadId(),
                entity.getResultado() == null ? null : entity.getResultado().name(),
                entity.getDetalle(),
                entity.getAntes(),
                entity.getDespues(),
                entity.getIp(),
                entity.getUserAgent(),
                entity.getRequestId()
        );
    }
}
