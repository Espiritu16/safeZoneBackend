package com.utp.safezonebackend.notificaciones.dto.request;

import com.utp.safezonebackend.notificaciones.enums.PrioridadNotificacion;
import com.utp.safezonebackend.notificaciones.enums.TipoNotificacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearNotificacionRequest(
        @NotBlank String usuarioId,
        String casoId,
        String denunciaId,
        @NotNull TipoNotificacion tipo,
        @NotNull PrioridadNotificacion prioridad,
        @NotBlank @Size(max = 160) String titulo,
        @NotBlank @Size(max = 2000) String mensaje
) {
}
