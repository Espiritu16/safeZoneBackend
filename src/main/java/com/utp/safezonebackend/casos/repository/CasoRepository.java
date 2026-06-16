package com.utp.safezonebackend.casos.repository;

import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CasoRepository extends JpaRepository<Caso, String> {
    List<Caso> findByVictimaIdAndActivoTrue(String victimaId);

    Optional<Caso> findByIdAndActivoTrue(String id);

    List<Caso> findByActivoTrueOrderByFechaCreacionDesc();

    List<Caso> findByVictimaIdAndActivoTrueOrderByFechaCreacionDesc(String victimaId);

    List<Caso> findByEstadoAndActivoTrueOrderByFechaCreacionDesc(EstadoCaso estado);

    List<Caso> findByPrioridadAndActivoTrueOrderByFechaCreacionDesc(PrioridadCaso prioridad);
}
