package com.utp.safezonebackend.casos.entity;

import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "caso")
public class Caso {

    @Id
    private String id;

    @Column(name = "victima_id")
    private String victimaId;

    @Enumerated(EnumType.STRING)
    private EstadoCaso estado;

    @Enumerated(EnumType.STRING)
    private PrioridadCaso prioridad;
    private String resumen;
    private String distrito;
    private boolean activo;
    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @Column(name = "fecha_cierre")
    private OffsetDateTime fechaCierre;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @Column(name = "creado_por")
    private String creadoPor;

    @Column(name = "actualizado_por")
    private String actualizadoPor;

    @Column(name = "inactivado_por")
    private String inactivadoPor;

    @Column(name = "fecha_inactivacion")
    private OffsetDateTime fechaInactivacion;
    public Caso() {
    }

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

    public EstadoCaso getEstado() {
        return estado;
    }

    public void setEstado(EstadoCaso estado) {
        this.estado = estado;
    }

    public PrioridadCaso getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadCaso prioridad) {
        this.prioridad = prioridad;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public OffsetDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(OffsetDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public OffsetDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(OffsetDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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

    public OffsetDateTime getFechaInactivacion() {
        return fechaInactivacion;
    }
    public void setFechaInactivacion(OffsetDateTime fechaInactivacion) {
        this.fechaInactivacion = fechaInactivacion;
    }
}
