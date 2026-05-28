package com.utp.safezonebackend.usuarios.dto.response;

import java.time.OffsetDateTime;

public record UsuarioResponse(
        String id,
        String correo,
        String nombres,
        String apellidos,
        String dni,
        String telefono,
        String distrito,
        String rol,
        boolean activo,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion,
        String creadoPor,
        String actualizadoPor,
        String inactivadoPor,
        OffsetDateTime fechaInactivacion
) {
}
