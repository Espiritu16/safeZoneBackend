package com.utp.safezonebackend.configuracion.repository;

import com.utp.safezonebackend.configuracion.entity.ConfiguracionSistema;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Long> {

    Optional<ConfiguracionSistema> findByClaveIgnoreCaseAndActivoTrue(String clave);

    boolean existsByClaveIgnoreCase(String clave);
}
