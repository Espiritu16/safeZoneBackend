package com.utp.safezonebackend.denuncias.mapper;

import com.utp.safezonebackend.denuncias.dto.response.DenunciaResponse;
import com.utp.safezonebackend.denuncias.entity.Denuncia;
import org.springframework.stereotype.Component;

@Component
public class DenunciaMapper {

    public DenunciaResponse toResponse(Denuncia entity) {
        return new DenunciaResponse();
    }
}
