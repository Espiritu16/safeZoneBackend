package com.utp.safezonebackend.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SolicitudRenovarToken(
        @NotBlank(message = "El refreshToken es obligatorio")
        String refreshToken
) {
}
