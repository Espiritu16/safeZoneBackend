package com.utp.safezonebackend.auth.repository;

import com.utp.safezonebackend.auth.entity.RecuperacionContrasenaCodigo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecuperacionContrasenaCodigoRepository extends JpaRepository<RecuperacionContrasenaCodigo, String> {
    List<RecuperacionContrasenaCodigo> findByUsuarioIdAndActivoTrueAndUsadoFalse(String usuarioId);

    Optional<RecuperacionContrasenaCodigo> findTopByUsuarioIdAndActivoTrueAndUsadoFalseOrderByFechaCreacionDesc(String usuarioId);

    Optional<RecuperacionContrasenaCodigo> findTopByUsuarioIdAndCodigoHashAndActivoTrueAndUsadoFalseOrderByFechaCreacionDesc(
            String usuarioId,
            String codigoHash
    );
}

