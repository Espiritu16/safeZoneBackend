package com.utp.safezonebackend.denuncias.repository;

import com.utp.safezonebackend.denuncias.entity.Denuncia;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DenunciaRepository extends JpaRepository<Denuncia, String> {
    List<Denuncia> findByVictimaIdAndActivoTrue(String victimaId);

    Optional<Denuncia> findByIdAndActivoTrue(String id);

    List<Denuncia> findByActivoTrueOrderByFechaCreacionDesc();

    List<Denuncia> findByCasoIdAndActivoTrue(String casoId);

    List<Denuncia> findByCasoIdInAndActivoTrueOrderByFechaCreacionDesc(List<String> casoIds);

    List<Denuncia> findByNivelRiesgoAndActivoTrue(NivelRiesgo nivelRiesgo);

    List<Denuncia> findByCasoIdInAndNivelRiesgoAndActivoTrue(List<String> casoIds, NivelRiesgo nivelRiesgo);
}
