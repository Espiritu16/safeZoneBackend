package com.utp.safezonebackend.citas.dto.request;

import com.utp.safezonebackend.citas.enums.TipoCita;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record CrearCitaRequest(
        @NotBlank String casoId,
        @NotNull TipoCita tipoCita,
        @NotNull OffsetDateTime fechaInicio,
        OffsetDateTime fechaFin,
        @Size(max = 2000) String observaciones
) {
}
