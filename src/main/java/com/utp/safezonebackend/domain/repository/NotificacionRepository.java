package com.utp.safezonebackend.domain.repository;

import com.utp.safezonebackend.persistance.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, String> {
}
