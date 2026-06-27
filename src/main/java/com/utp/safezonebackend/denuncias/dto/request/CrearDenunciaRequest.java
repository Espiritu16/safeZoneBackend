package com.utp.safezonebackend.denuncias.dto.request;

import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

public record CrearDenunciaRequest(
        String casoId,
        @NotBlank String victimaId,
        @NotBlank @Size(max = 2000) String descripcion,
        @NotBlank @Size(max = 100) String tipoViolencia,
        OffsetDateTime fechaIncidente,
        @NotBlank @Size(max = 120) String distrito,
        @Size(max = 500) String direccionReferencia,
        @NotNull NivelRiesgo nivelRiesgo,
        Boolean anonima,
        List<String> adjuntos,
        Integer edad
) {
}
