package com.utp.safezonebackend.asignaciones.dto.request;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;

public record ActualizarAsignacionCasoRequest(
        String profesionalId,
        RolUsuario rolProfesional,
        Boolean activo
) {
}
