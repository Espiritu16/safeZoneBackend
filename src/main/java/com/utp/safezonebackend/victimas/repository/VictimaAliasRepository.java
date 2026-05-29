package com.utp.safezonebackend.victimas.repository;

import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VictimaAliasRepository extends JpaRepository<VictimaAlias, String> {
    Optional<VictimaAlias> findTopByAliasCodigoIgnoreCaseAndActivoTrueOrderByFechaAsignacionDesc(String aliasCodigo);

    Optional<VictimaAlias> findTopByVictimaIdAndActivoTrueOrderByFechaAsignacionDesc(String victimaId);
}
