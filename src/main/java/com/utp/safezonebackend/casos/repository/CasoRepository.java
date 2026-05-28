package com.utp.safezonebackend.casos.repository;

import com.utp.safezonebackend.casos.entity.Caso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CasoRepository extends JpaRepository<Caso, String> {
    List<Caso> findByVictimaIdAndEliminadoFalse(String victimaId);
}
