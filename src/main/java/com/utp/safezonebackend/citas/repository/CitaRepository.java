package com.utp.safezonebackend.citas.repository;

import com.utp.safezonebackend.citas.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, String> {
    List<Cita> findByVictimaIdAndEliminadoFalse(String victimaId);
}
