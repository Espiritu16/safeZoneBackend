package com.utp.safezonebackend.domain.repository;

import com.utp.safezonebackend.persistance.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitaRepository extends JpaRepository<Cita, String> {
}
