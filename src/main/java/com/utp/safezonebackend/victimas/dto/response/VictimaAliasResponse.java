package com.utp.safezonebackend.victimas.dto.response;

import java.time.OffsetDateTime;

public class VictimaAliasResponse {
    private String aliasCodigo;
    private String creadoPor;
    private OffsetDateTime fechaAsignacion;
    private OffsetDateTime fechaFin;

    public String getAliasCodigo() {
        return aliasCodigo;
    }

    public void setAliasCodigo(String aliasCodigo) {
        this.aliasCodigo = aliasCodigo;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public OffsetDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(OffsetDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public OffsetDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(OffsetDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
}
