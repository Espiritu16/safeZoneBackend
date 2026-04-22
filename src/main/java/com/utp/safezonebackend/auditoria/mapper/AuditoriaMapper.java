package com.utp.safezonebackend.auditoria.mapper;

import com.utp.safezonebackend.auditoria.dto.response.AuditoriaResponse;
import com.utp.safezonebackend.auditoria.entity.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaResponse toResponse(Auditoria entity) {
        return new AuditoriaResponse();
    }
}
