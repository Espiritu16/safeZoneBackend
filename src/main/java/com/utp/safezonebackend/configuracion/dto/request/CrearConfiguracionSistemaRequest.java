package com.utp.safezonebackend.configuracion.dto.request;

import com.utp.safezonebackend.configuracion.enums.TipoValorConfiguracion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearConfiguracionSistemaRequest(
        @NotBlank(message = "La clave es obligatoria")
        @Size(max = 100, message = "La clave no debe superar 100 caracteres")
        String clave,

        @NotBlank(message = "El valor es obligatorio")
        @Size(max = 500, message = "El valor no debe superar 500 caracteres")
        String valor,

        @NotNull(message = "El tipo de valor es obligatorio")
        TipoValorConfiguracion tipoValor,

        @Size(max = 255, message = "La descripcion no debe superar 255 caracteres")
        String descripcion
) {
}
