package com.utp.safezonebackend.seguimientos.repository;

import com.utp.safezonebackend.seguimientos.entity.SeguimientoCaso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeguimientoCasoRepository extends JpaRepository<SeguimientoCaso, String> {
    List<SeguimientoCaso> findByCasoIdInAndEliminadoFalse(List<String> casoIds);
}
