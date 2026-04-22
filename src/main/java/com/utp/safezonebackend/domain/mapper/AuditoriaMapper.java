package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.AuditoriaResponse;
import com.utp.safezonebackend.persistance.entity.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaResponse toResponse(Auditoria entity) {
        return new AuditoriaResponse();
    }
}
