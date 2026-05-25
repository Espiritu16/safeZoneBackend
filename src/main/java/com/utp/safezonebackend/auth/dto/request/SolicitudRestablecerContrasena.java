package com.utp.safezonebackend.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SolicitudRestablecerContrasena(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato valido")
        String correo,

        @NotBlank(message = "El codigo es obligatorio")
        @Pattern(regexp = "^\\d{6}$", message = "El codigo debe tener 6 digitos")
        String codigo,

        @NotBlank(message = "La nueva contrasena es obligatoria")
        @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
                message = "La contrasena debe incluir al menos una mayuscula y un numero"
        )
        String nuevaPassword
) {
}

