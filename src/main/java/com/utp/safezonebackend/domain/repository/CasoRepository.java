package com.utp.safezonebackend.domain.repository;

import com.utp.safezonebackend.persistance.entity.Caso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CasoRepository extends JpaRepository<Caso, String> {
}
