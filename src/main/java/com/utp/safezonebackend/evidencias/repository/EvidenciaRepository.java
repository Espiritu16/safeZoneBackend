package com.utp.safezonebackend.evidencias.repository;

import com.utp.safezonebackend.evidencias.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvidenciaRepository extends JpaRepository<Evidencia, String> {
}
