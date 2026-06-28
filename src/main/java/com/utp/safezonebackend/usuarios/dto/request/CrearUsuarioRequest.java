package com.utp.safezonebackend.usuarios.dto.request;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequest(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 254, message = "El correo no debe superar 254 caracteres")
        String correo,

        @NotBlank(message = "La contrasena es obligatoria")
        @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
                message = "La contrasena debe incluir al menos una mayuscula y un numero"
        )
        String contrasena,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 120, message = "Los nombres no deben superar 120 caracteres")
        @Pattern(
                regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü' -]+$",
                message = "Los nombres solo pueden contener letras"
        )
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 120, message = "Los apellidos no deben superar 120 caracteres")
        @Pattern(
                regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü' -]+$",
                message = "Los apellidos solo pueden contener letras"
        )
        String apellidos,

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 digitos")
        String dni,

        @Pattern(regexp = "^$|^\\d{9}$", message = "El telefono debe tener 9 digitos")
        String telefono,

        @Size(max = 120, message = "El distrito no debe superar 120 caracteres")
        String distrito,

        @NotNull(message = "El rol es obligatorio")
        RolUsuario rol
) {
}
