package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.CitaResponse;
import com.utp.safezonebackend.persistance.entity.Cita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public CitaResponse toResponse(Cita entity) {
        return new CitaResponse();
    }
}
