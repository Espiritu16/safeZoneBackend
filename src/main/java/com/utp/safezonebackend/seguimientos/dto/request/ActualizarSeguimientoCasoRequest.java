package com.utp.safezonebackend.seguimientos.dto.request;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record ActualizarSeguimientoCasoRequest(
        @Size(max = 100) String tipoSeguimiento,
        @Size(max = 3000) String contenido,
        @Size(max = 1000) String proximaAccion,
        OffsetDateTime fechaProximaAccion
) {
}
