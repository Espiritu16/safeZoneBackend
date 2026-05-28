package com.utp.safezonebackend.auth.dto.response;

public record RespuestaLogin(
        boolean success,
        String message,
        String usuarioId,
        String nombre,
        String correo,
        String rol,
        String token,
        String refreshToken,
        String tipoToken
) {
}
