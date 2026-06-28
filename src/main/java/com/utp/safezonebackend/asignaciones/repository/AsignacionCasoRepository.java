package com.utp.safezonebackend.asignaciones.repository;

import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignacionCasoRepository extends JpaRepository<AsignacionCaso, String> {
    List<AsignacionCaso> findByActivoTrueOrderByFechaAsignacionDesc();

    List<AsignacionCaso> findByCasoIdAndActivoTrueOrderByFechaAsignacionDesc(String casoId);

    Optional<AsignacionCaso> findByIdAndActivoTrue(String id);

    Optional<AsignacionCaso> findTopByCasoIdAndRolProfesionalAndActivoTrueOrderByFechaAsignacionDesc(
            String casoId,
            RolUsuario rolProfesional
    );

    boolean existsByCasoIdAndProfesionalIdAndRolProfesionalAndActivoTrue(
            String casoId,
            String profesionalId,
            RolUsuario rolProfesional
    );
}
