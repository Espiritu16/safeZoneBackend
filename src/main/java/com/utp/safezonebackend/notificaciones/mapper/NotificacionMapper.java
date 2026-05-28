package com.utp.safezonebackend.notificaciones.mapper;

import com.utp.safezonebackend.notificaciones.dto.response.NotificacionResponse;
import com.utp.safezonebackend.notificaciones.entity.Notificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public NotificacionResponse toResponse(Notificacion entity) {
        return new NotificacionResponse();
    }
}
