package com.utp.safezonebackend.configuracion.dto.response;

import com.utp.safezonebackend.configuracion.enums.TipoValorConfiguracion;
import java.time.OffsetDateTime;

public record ConfiguracionSistemaResponse(
        Long id,
        String clave,
        String valor,
        TipoValorConfiguracion tipoValor,
        String descripcion,
        boolean activo,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion,
        OffsetDateTime fechaInactivacion,
        String creadoPor,
        String actualizadoPor,
        String inactivadoPor
) {
}
