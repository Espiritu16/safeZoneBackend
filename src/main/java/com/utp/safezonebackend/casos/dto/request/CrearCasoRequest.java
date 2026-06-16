package com.utp.safezonebackend.casos.dto.request;

import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearCasoRequest(
        @NotBlank String victimaId,
        @NotBlank @Size(max = 1000) String resumen,
        @NotBlank @Size(max = 120) String distrito,
        @NotNull PrioridadCaso prioridad,
        EstadoCaso estado
) {
}
