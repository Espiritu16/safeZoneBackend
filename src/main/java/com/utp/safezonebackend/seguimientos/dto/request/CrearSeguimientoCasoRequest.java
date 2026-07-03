package com.utp.safezonebackend.seguimientos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record CrearSeguimientoCasoRequest(
        @NotBlank String casoId,
        @NotBlank @Size(max = 100) String tipoSeguimiento,
        @NotBlank @Size(max = 3000) String contenido,
        @Size(max = 1000) String proximaAccion,
        OffsetDateTime fechaProximaAccion
) {
}
