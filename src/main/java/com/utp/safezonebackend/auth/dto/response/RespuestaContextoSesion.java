package com.utp.safezonebackend.auth.dto.response;

import java.util.List;

public record RespuestaContextoSesion(
        boolean success,
        String message,
        String usuarioId,
        String nombre,
        String correo,
        String rol,
        List<String> permisos,
        List<String> modulos
) {
}
