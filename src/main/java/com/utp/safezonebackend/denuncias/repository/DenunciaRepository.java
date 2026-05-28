package com.utp.safezonebackend.denuncias.repository;

import com.utp.safezonebackend.denuncias.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, String> {
    List<Denuncia> findByVictimaIdAndEliminadoFalse(String victimaId);
}
