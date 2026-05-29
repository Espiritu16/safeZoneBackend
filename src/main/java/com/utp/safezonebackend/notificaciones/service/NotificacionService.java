package com.utp.safezonebackend.notificaciones.service;

import com.utp.safezonebackend.notificaciones.dto.request.CrearNotificacionRequest;
import com.utp.safezonebackend.notificaciones.dto.request.ActualizarNotificacionRequest;
import com.utp.safezonebackend.notificaciones.dto.response.NotificacionResponse;
import com.utp.safezonebackend.notificaciones.mapper.NotificacionMapper;
import com.utp.safezonebackend.notificaciones.repository.NotificacionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NotificacionService {

    private final NotificacionRepository repository;
    private final NotificacionMapper mapper;

    public NotificacionService(NotificacionRepository repository, NotificacionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<NotificacionResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public NotificacionResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public NotificacionResponse create(CrearNotificacionRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public NotificacionResponse update(String id, ActualizarNotificacionRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }
}
