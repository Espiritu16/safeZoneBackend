package com.utp.safezonebackend.seguimientos.dto.response;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import java.time.OffsetDateTime;

public record SeguimientoCasoResponse(
        String id,
        String casoId,
        String autorId,
        RolUsuario rolAutor,
        String tipoSeguimiento,
        String contenido,
        String proximaAccion,
        OffsetDateTime fechaProximaAccion,
        boolean activo,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion
) {
}
