package com.utp.safezonebackend.predenuncias.dto.request;

import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record FormalizarPreDenunciaRequest(
        String victimaId,
        String denunciaId,
        String casoId,
        NivelRiesgo nivelRiesgo,
        Boolean formalizarAnonima,
        @NotNull(message = "La edad de la victima es obligatoria")
        @Min(value = 1, message = "La edad debe ser mayor a 0")
        @Max(value = 120, message = "La edad no debe superar 120")
        Integer edad
) {
}
