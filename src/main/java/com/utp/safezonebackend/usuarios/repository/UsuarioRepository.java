package com.utp.safezonebackend.usuarios.repository;

import com.utp.safezonebackend.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
