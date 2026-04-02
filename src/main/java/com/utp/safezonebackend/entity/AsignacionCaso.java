package com.utp.safezonebackend.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.utp.safezonebackend.entity.enums.RolUsuario;
import java.time.OffsetDateTime;
import org.springframework.data.annotation.Id;

@Container(containerName = "asignaciones_caso")
public class AsignacionCaso {

    @Id
    private String id;

    @PartitionKey
    private String casoId;

    private String profesionalId;
    private RolUsuario rolProfesional;
    private boolean activo;
    private OffsetDateTime fechaAsignacion;
    private OffsetDateTime fechaFin;
    private String asignadoPor;

    public AsignacionCaso() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCasoId() {
        return casoId;
    }

    public void setCasoId(String casoId) {
        this.casoId = casoId;
    }

    public String getProfesionalId() {
        return profesionalId;
    }

    public void setProfesionalId(String profesionalId) {
        this.profesionalId = profesionalId;
    }

    public RolUsuario getRolProfesional() {
        return rolProfesional;
    }

    public void setRolProfesional(RolUsuario rolProfesional) {
        this.rolProfesional = rolProfesional;
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

    public String getAsignadoPor() {
        return asignadoPor;
    }

    public void setAsignadoPor(String asignadoPor) {
        this.asignadoPor = asignadoPor;
    }
}
