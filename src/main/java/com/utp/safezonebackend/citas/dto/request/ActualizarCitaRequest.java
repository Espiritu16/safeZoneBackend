package com.utp.safezonebackend.citas.dto.request;

import com.utp.safezonebackend.citas.enums.EstadoCita;
import com.utp.safezonebackend.citas.enums.TipoCita;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record ActualizarCitaRequest(
        TipoCita tipoCita,
        OffsetDateTime fechaInicio,
        OffsetDateTime fechaFin,
        EstadoCita estado,
        @Size(max = 255) String motivoCancelacion,
        @Size(max = 2000) String observaciones
) {
}
