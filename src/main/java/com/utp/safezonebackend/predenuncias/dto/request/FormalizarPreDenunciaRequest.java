package com.utp.safezonebackend.predenuncias.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FormalizarPreDenunciaRequest(
        @NotBlank String victimaId,
        @NotBlank String denunciaId,
        String casoId
) {
}
