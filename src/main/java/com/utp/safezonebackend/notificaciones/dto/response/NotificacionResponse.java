package com.utp.safezonebackend.notificaciones.dto.response;

import com.utp.safezonebackend.notificaciones.enums.PrioridadNotificacion;
import com.utp.safezonebackend.notificaciones.enums.TipoNotificacion;
import java.time.OffsetDateTime;

public record NotificacionResponse(
        String id,
        String usuarioId,
        String casoId,
        String denunciaId,
        TipoNotificacion tipo,
        PrioridadNotificacion prioridad,
        String titulo,
        String mensaje,
        boolean leida,
        OffsetDateTime fechaLectura,
        boolean activo,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion
) {
}
