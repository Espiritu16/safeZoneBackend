package com.utp.safezonebackend.domain.repository;

import com.utp.safezonebackend.persistance.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
