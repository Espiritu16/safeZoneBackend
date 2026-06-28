package com.utp.safezonebackend.usuarios.dto.request;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ActualizarUsuarioRequest(
        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 254, message = "El correo no debe superar 254 caracteres")
        String correo,

        @Size(min = 1, max = 120, message = "Los nombres deben tener entre 1 y 120 caracteres")
        @Pattern(
                regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü' -]+$",
                message = "Los nombres solo pueden contener letras"
        )
        String nombres,

        @Size(min = 1, max = 120, message = "Los apellidos deben tener entre 1 y 120 caracteres")
        @Pattern(
                regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü' -]+$",
                message = "Los apellidos solo pueden contener letras"
        )
        String apellidos,

        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 digitos")
        String dni,

        @Pattern(regexp = "^$|^\\d{9}$", message = "El telefono debe tener 9 digitos")
        String telefono,

        @Size(max = 120, message = "El distrito no debe superar 120 caracteres")
        String distrito,

        RolUsuario rol,
        Boolean activo
) {
}
