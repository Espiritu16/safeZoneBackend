package com.utp.safezonebackend.auth.repository;

import com.utp.safezonebackend.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
