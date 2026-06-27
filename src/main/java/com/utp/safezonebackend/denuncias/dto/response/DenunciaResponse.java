package com.utp.safezonebackend.denuncias.dto.response;

import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import java.time.OffsetDateTime;
import java.util.List;

public record DenunciaResponse(
        String id,
        String casoId,
        String victimaId,
        String descripcion,
        String tipoViolencia,
        OffsetDateTime fechaIncidente,
        String distrito,
        String direccionReferencia,
        NivelRiesgo nivelRiesgo,
        boolean anonima,
        List<String> adjuntos,
        boolean activo,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion,
        Integer edad
) {
}
