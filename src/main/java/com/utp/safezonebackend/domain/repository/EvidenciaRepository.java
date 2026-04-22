package com.utp.safezonebackend.domain.repository;

import com.utp.safezonebackend.persistance.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvidenciaRepository extends JpaRepository<Evidencia, String> {
}
