package com.utp.safezonebackend.notificaciones.repository;

import com.utp.safezonebackend.notificaciones.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificacionRepository extends JpaRepository<Notificacion, String> {
    Optional<Notificacion> findByIdAndActivoTrue(String id);

    List<Notificacion> findByActivoTrueOrderByFechaCreacionDesc();

    List<Notificacion> findByUsuarioIdAndActivoTrueOrderByFechaCreacionDesc(String usuarioId);

    long countByUsuarioIdAndLeidaFalseAndActivoTrue(String usuarioId);
}
