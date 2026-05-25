package com.utp.safezonebackend.notificaciones.repository;

import com.utp.safezonebackend.notificaciones.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, String> {
}
