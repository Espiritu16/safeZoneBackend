package com.utp.safezonebackend.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.utp.safezonebackend.entity.enums.EstadoCaso;
import com.utp.safezonebackend.entity.enums.PrioridadCaso;
import java.time.OffsetDateTime;
import org.springframework.data.annotation.Id;

@Container(containerName = "casos")
public class Caso {

    @Id
    private String id;

    @PartitionKey
    private String victimaId;

    private EstadoCaso estado;
    private PrioridadCaso prioridad;
    private String resumen;
    private String distrito;
    private OffsetDateTime fechaCreacion;
    private OffsetDateTime fechaCierre;

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
}
