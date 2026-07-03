package com.utp.safezonebackend.evidencias.repository;

import com.utp.safezonebackend.evidencias.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvidenciaRepository extends JpaRepository<Evidencia, String> {
    List<Evidencia> findByCasoId(String casoId);
    List<Evidencia> findByDenunciaId(String denunciaId);
    List<Evidencia> findByPredenunciaId(String predenunciaId);
}
