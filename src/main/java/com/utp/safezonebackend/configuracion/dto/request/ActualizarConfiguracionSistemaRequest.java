package com.utp.safezonebackend.configuracion.dto.request;

import com.utp.safezonebackend.configuracion.enums.TipoValorConfiguracion;
import jakarta.validation.constraints.Size;

public record ActualizarConfiguracionSistemaRequest(
        @Size(max = 500, message = "El valor no debe superar 500 caracteres")
        String valor,

        TipoValorConfiguracion tipoValor,

        @Size(max = 255, message = "La descripcion no debe superar 255 caracteres")
        String descripcion,

        Boolean activo
) {
}
