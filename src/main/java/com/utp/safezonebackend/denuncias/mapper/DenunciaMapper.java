package com.utp.safezonebackend.denuncias.mapper;

import com.utp.safezonebackend.denuncias.dto.response.DenunciaResponse;
import com.utp.safezonebackend.denuncias.entity.Denuncia;
import org.springframework.stereotype.Component;

@Component
public class DenunciaMapper {

    public DenunciaResponse toResponse(Denuncia entity) {
        if (entity == null) {
            return null;
        }
        return new DenunciaResponse(
                entity.getId(),
                entity.getCasoId(),
                entity.getVictimaId(),
                entity.getDescripcion(),
                entity.getTipoViolencia(),
                entity.getFechaIncidente(),
                entity.getDistrito(),
                entity.getDireccionReferencia(),
                entity.getNivelRiesgo(),
                entity.isAnonima(),
                entity.getAdjuntos(),
                entity.isActivo(),
                entity.getFechaCreacion(),
                entity.getFechaActualizacion(),
                entity.getEdad()
        );
    }
}
