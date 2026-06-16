package com.utp.safezonebackend.seguimientos.repository;

import com.utp.safezonebackend.seguimientos.entity.SeguimientoCaso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeguimientoCasoRepository extends JpaRepository<SeguimientoCaso, String> {
    List<SeguimientoCaso> findByCasoIdInAndActivoTrue(List<String> casoIds);

    Optional<SeguimientoCaso> findByIdAndActivoTrue(String id);

    List<SeguimientoCaso> findByActivoTrueOrderByFechaCreacionDesc();

    List<SeguimientoCaso> findByCasoIdAndActivoTrueOrderByFechaCreacionDesc(String casoId);

    List<SeguimientoCaso> findByAutorIdAndActivoTrueOrderByFechaCreacionDesc(String autorId);
}
