package com.utp.safezonebackend.configuracion.mapper;

import com.utp.safezonebackend.configuracion.dto.response.ConfiguracionSistemaResponse;
import com.utp.safezonebackend.configuracion.entity.ConfiguracionSistema;
import org.springframework.stereotype.Component;

@Component
public class ConfiguracionSistemaMapper {

    public ConfiguracionSistemaResponse toResponse(ConfiguracionSistema entity) {
        return new ConfiguracionSistemaResponse();
    }
}
