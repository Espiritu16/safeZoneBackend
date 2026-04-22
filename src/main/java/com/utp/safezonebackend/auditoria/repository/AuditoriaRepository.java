package com.utp.safezonebackend.auditoria.repository;

import com.utp.safezonebackend.auditoria.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<Auditoria, String> {
}
