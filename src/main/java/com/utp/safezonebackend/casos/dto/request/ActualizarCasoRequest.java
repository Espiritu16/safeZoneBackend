package com.utp.safezonebackend.casos.dto.request;

import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import jakarta.validation.constraints.Size;

public record ActualizarCasoRequest(
        @Size(max = 1000) String resumen,
        @Size(max = 120) String distrito,
        PrioridadCaso prioridad,
        Boolean activo,
        EstadoCaso estado
) {
}
