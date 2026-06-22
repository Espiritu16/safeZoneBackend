package com.utp.safezonebackend.denuncias.dto.request;

import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

public record ActualizarDenunciaRequest(
        String casoId,
        @Size(max = 2000) String descripcion,
        @Size(max = 100) String tipoViolencia,
        OffsetDateTime fechaIncidente,
        @Size(max = 120) String distrito,
        @Size(max = 500) String direccionReferencia,
        NivelRiesgo nivelRiesgo,
        Boolean anonima,
        List<String> adjuntos
) {
}
