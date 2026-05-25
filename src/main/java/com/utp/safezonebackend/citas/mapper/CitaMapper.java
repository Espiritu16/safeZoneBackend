package com.utp.safezonebackend.citas.mapper;

import com.utp.safezonebackend.citas.dto.response.CitaResponse;
import com.utp.safezonebackend.citas.entity.Cita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public CitaResponse toResponse(Cita entity) {
        return new CitaResponse();
    }
}
