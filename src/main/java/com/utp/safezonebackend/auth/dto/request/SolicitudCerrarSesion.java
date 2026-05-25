package com.utp.safezonebackend.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SolicitudCerrarSesion(
        @NotBlank(message = "El refreshToken es obligatorio")
        String refreshToken
) {
}
