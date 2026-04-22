package com.utp.safezonebackend.domain.mapper;

import com.utp.safezonebackend.domain.dto.response.NotificacionResponse;
import com.utp.safezonebackend.persistance.entity.Notificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public NotificacionResponse toResponse(Notificacion entity) {
        return new NotificacionResponse();
    }
}
