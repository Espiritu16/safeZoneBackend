package com.utp.safezonebackend.victimas.dto.response;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record VictimaHistorialResponse(
        String victimaId,
        String aliasActivo,
        List<HistorialItem> denuncias,
        List<HistorialItem> citas,
        List<HistorialItem> seguimientos,
        List<HistorialItem> evidencias,
        List<HistorialItem> lineaTiempo
) {
    public record HistorialItem(
            String tipo,
            String id,
            String casoId,
            String titulo,
            String detalle,
            String estado,
            OffsetDateTime fecha,
            Map<String, Object> metadata
    ) {
    }
}
