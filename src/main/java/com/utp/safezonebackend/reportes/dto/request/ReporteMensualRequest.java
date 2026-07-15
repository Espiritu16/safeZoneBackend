package com.utp.safezonebackend.reportes.dto.request;

import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import java.time.OffsetDateTime;

public record ReporteMensualRequest(
        OffsetDateTime fechaDesde,
        OffsetDateTime fechaHasta,
        String tipoViolencia,
        NivelRiesgo nivelRiesgo
) {
}
