package com.utp.safezonebackend.citas.dto.response;

import com.utp.safezonebackend.citas.enums.EstadoCita;
import com.utp.safezonebackend.citas.enums.TipoCita;
import java.time.OffsetDateTime;

public record CitaResponse(
        String id,
        String casoId,
        String victimaId,
        String especialistaId,
        TipoCita tipoCita,
        OffsetDateTime fechaInicio,
        OffsetDateTime fechaFin,
        EstadoCita estado,
        String motivoCancelacion,
        String observaciones,
        boolean reprogramada,
        boolean activo,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion
) {
}
