package com.utp.safezonebackend.auth.repository;

import com.utp.safezonebackend.auth.entity.RefreshToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findTopByTokenHashAndActivoTrueAndRevocadoFalseOrderByFechaCreacionDesc(String tokenHash);

    List<RefreshToken> findByUsuarioIdAndActivoTrueAndRevocadoFalse(String usuarioId);
}
