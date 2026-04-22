package com.utp.safezonebackend.domain.repository;

import com.utp.safezonebackend.persistance.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, String> {
}
