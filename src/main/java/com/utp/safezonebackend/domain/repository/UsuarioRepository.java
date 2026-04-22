package com.utp.safezonebackend.domain.repository;

import com.utp.safezonebackend.persistance.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
