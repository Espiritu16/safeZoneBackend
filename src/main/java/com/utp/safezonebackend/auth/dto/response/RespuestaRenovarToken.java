package com.utp.safezonebackend.auth.dto.response;

public record RespuestaRenovarToken(
        boolean success,
        String message,
        String token,
        String refreshToken,
        String tipoToken
) {
}
