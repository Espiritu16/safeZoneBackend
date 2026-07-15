package com.utp.safezonebackend.notificaciones.dto.request;

import com.utp.safezonebackend.notificaciones.enums.PrioridadNotificacion;
import jakarta.validation.constraints.Size;

public record ActualizarNotificacionRequest(
        PrioridadNotificacion prioridad,
        @Size(max = 160) String titulo,
        @Size(max = 2000) String mensaje,
        Boolean leida,
        Boolean activo
) {
}
