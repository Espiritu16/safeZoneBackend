package com.utp.safezonebackend.asignaciones.dto.response;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import java.time.OffsetDateTime;

public record AsignacionCasoResponse(
        String id,
        String casoId,
        String profesionalId,
        RolUsuario rolProfesional,
        boolean activo,
        OffsetDateTime fechaAsignacion,
        OffsetDateTime fechaFin,
        String asignadoPor,
        OffsetDateTime fechaActualizacion,
        String actualizadoPor,
        String inactivadoPor,
        OffsetDateTime fechaInactivacion
) {
}
