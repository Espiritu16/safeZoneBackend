package com.utp.safezonebackend.citas.repository;

import com.utp.safezonebackend.citas.entity.Cita;
import com.utp.safezonebackend.citas.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface CitaRepository extends JpaRepository<Cita, String> {
    List<Cita> findByVictimaIdAndActivoTrue(String victimaId);

    Optional<Cita> findByIdAndActivoTrue(String id);

    List<Cita> findByActivoTrueOrderByFechaInicioDesc();

    List<Cita> findByEspecialistaIdAndActivoTrueOrderByFechaInicioDesc(String especialistaId);

    @Query("""
            select c from Cita c
            where c.especialistaId = :especialistaId
              and c.activo = true
              and c.estado not in :estadosExcluidos
              and c.fechaInicio < :fechaFin
              and c.fechaFin > :fechaInicio
            """)
    List<Cita> findSolapadasPorEspecialista(
            @Param("especialistaId") String especialistaId,
            @Param("fechaInicio") OffsetDateTime fechaInicio,
            @Param("fechaFin") OffsetDateTime fechaFin,
            @Param("estadosExcluidos") List<EstadoCita> estadosExcluidos
    );
}
