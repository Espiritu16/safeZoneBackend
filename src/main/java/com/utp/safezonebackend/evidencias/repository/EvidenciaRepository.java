package com.utp.safezonebackend.evidencias.repository;

import com.utp.safezonebackend.evidencias.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenciaRepository extends JpaRepository<Evidencia, String> {
    List<Evidencia> findByCasoIdInAndEliminadoFalse(List<String> casoIds);
    List<Evidencia> findByDenunciaIdInAndEliminadoFalse(List<String> denunciaIds);
}
