package com.utp.safezonebackend.predenuncias.repository;

import com.utp.safezonebackend.predenuncias.entity.PreDenuncia;
import com.utp.safezonebackend.predenuncias.enums.EstadoPreDenuncia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreDenunciaRepository extends JpaRepository<PreDenuncia, String> {
    List<PreDenuncia> findByActivoTrueOrderByFechaCreacionDesc();

    List<PreDenuncia> findByEstadoAndActivoTrueOrderByFechaCreacionDesc(EstadoPreDenuncia estado);

    List<PreDenuncia> findByVictimaIdAndActivoTrueOrderByFechaCreacionDesc(String victimaId);

    long countByEstadoAndActivoTrue(EstadoPreDenuncia estado);
}
