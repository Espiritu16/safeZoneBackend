package com.utp.safezonebackend.domain.repository;

import com.utp.safezonebackend.persistance.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaRepository extends JpaRepository<Denuncia, String> {
}
