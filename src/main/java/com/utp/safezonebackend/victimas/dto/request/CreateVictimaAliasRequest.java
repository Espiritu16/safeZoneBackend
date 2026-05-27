package com.utp.safezonebackend.victimas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class CreateVictimaAliasRequest {
    @NotBlank
    private String victimaId;
    @NotBlank
    private String creadoPor;
    @NotNull
    private OffsetDateTime fechaFin;
    public String getVictimaId() {
        return victimaId;
    }
    public void setVictimaId(String victimaId) {
        this.victimaId = victimaId;
    }
    public String getCreadoPor() {
        return creadoPor;
    }
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
    public OffsetDateTime getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(OffsetDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
}
