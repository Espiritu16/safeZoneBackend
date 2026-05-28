package com.utp.safezonebackend.usuarios.dto.request;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import jakarta.validation.constraints.Email;

public record UpdateUsuarioRequest(
        @Email(message = "El correo no tiene un formato valido")
        String correo,
        String nombres,
        String apellidos,
        String dni,
        String telefono,
        String distrito,
        RolUsuario rol,
        Boolean activo
) {
}
