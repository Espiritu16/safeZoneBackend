package com.utp.safezonebackend.asignaciones.dto.request;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearAsignacionCasoRequest(
        @NotBlank(message = "El caso es obligatorio")
        String casoId,

        @NotBlank(message = "El profesional es obligatorio")
        String profesionalId,

        @NotNull(message = "El rol profesional es obligatorio")
        RolUsuario rolProfesional
) {
}
