package com.utp.safezonebackend.usuarios.repository;

import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, String id);

    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, String id);

    long countByRolAndActivoTrue(RolUsuario rol);

    List<Usuario> findByIdIn(Collection<String> ids);

    List<Usuario> findByRolInAndActivoTrueOrderByNombresAscApellidosAsc(Collection<RolUsuario> roles);

    default Optional<Usuario> buscarPorCorreo(String correo) {
        return findByCorreoIgnoreCase(correo);
    }

    default boolean existePorCorreo(String correo) {
        return existsByCorreoIgnoreCase(correo);
    }
}
