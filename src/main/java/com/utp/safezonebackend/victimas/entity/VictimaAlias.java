package com.utp.safezonebackend.victimas.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "victimas_alias")
public class VictimaAlias {

    @Id
    private String id;

    @Column(name = "victima_id")
    @OneToOne
    @JoinColumn(name="id_usuario")
    private String victimaId;

    @Column(name = "alias_codigo")
    private String aliasCodigo;

    private boolean activo;

    @Column(name = "fecha_asignacion")
    private OffsetDateTime fechaAsignacion;

    @Column(name = "fecha_fin")
    private OffsetDateTime fechaFin;

    @Column(name = "creado_por")
    private String creadoPor;

    @Column(name = "actualizado_por")
    private String actualizadoPor;

    @Column(name = "inactivado_por")
    private String inactivadoPor;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @Column(name = "fecha_inactivacion")
    private OffsetDateTime fechaInactivacion;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVictimaId() {
        return victimaId;
    }
    public void setVictimaId(String victimaId) {
        this.victimaId = victimaId;
    }
    public String getAliasCodigo() {
        return aliasCodigo;
    }
    public void setAliasCodigo(String aliasCodigo) {
        this.aliasCodigo = aliasCodigo;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
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
    public String getCreadoPor() {
        return creadoPor;
    }
    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
    public String getActualizadoPor() {
        return actualizadoPor;
    }
    public void setActualizadoPor(String actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }
    public String getInactivadoPor() {
        return inactivadoPor;
    }
    public void setInactivadoPor(String inactivadoPor) {
        this.inactivadoPor = inactivadoPor;
    }
    public OffsetDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
    public void setFechaActualizacion(OffsetDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
    public OffsetDateTime getFechaInactivacion() {
        return fechaInactivacion;
    }
    public void setFechaInactivacion(OffsetDateTime fechaInactivacion) {
        this.fechaInactivacion = fechaInactivacion;
    }
}
