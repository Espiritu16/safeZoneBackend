package com.utp.safezonebackend.reportes.dto.response;

import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.citas.enums.EstadoCita;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import java.time.OffsetDateTime;
import java.util.Map;

public record ReporteMensualResponse(
        OffsetDateTime fechaDesde,
        OffsetDateTime fechaHasta,
        long totalDenuncias,
        long totalCasos,
        long totalCitas,
        long citasAtendidas,
        long citasCanceladas,
        long citasNoAsistidas,
        Map<String, Long> porTipoViolencia,
        Map<NivelRiesgo, Long> porNivelRiesgo,
        Map<String, Long> porDistrito,
        Map<EstadoCaso, Long> casosPorEstado,
        Map<EstadoCita, Long> citasPorEstado
) {
}
