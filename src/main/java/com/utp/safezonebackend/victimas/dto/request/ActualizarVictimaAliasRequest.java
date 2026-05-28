package com.utp.safezonebackend.victimas.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public class ActualizarVictimaAliasRequest {
    @NotBlank
    private String actualizadoPor;
    public String getActualizadoPor() {
        return actualizadoPor;
    }
    public void setActualizadoPor(String actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }
}
