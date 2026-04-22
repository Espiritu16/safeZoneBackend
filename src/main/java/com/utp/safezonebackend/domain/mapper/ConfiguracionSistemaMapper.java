package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.ConfiguracionSistemaResponse;
import com.utp.safezonebackend.persistance.entity.ConfiguracionSistema;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionSistemaMapper {

    public ConfiguracionSistemaResponse toResponse(ConfiguracionSistema entity) {
        return new ConfiguracionSistemaResponse();
    }
}
