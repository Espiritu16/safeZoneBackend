package com.utp.safezonebackend.casos.dto.response;

import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import java.time.OffsetDateTime;

public record CasoResponse(
        String id,
        String victimaId,
        EstadoCaso estado,
        PrioridadCaso prioridad,
        String resumen,
        String distrito,
        boolean activo,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaCierre,
        OffsetDateTime fechaActualizacion
) {
}
