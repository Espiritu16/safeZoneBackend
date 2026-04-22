package com.utp.safezonebackend.asignaciones.entity;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "asignaciones_caso")
public class AsignacionCaso {

    @Id
    private String id;

    @Column(name = "caso_id")
    private String casoId;

    @Column(name = "profesional_id")
    private String profesionalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_profesional")
    private RolUsuario rolProfesional;

    private boolean activo;

    private boolean eliminado;

    @Column(name = "fecha_asignacion")
    private OffsetDateTime fechaAsignacion;

    @Column(name = "fecha_fin")
    private OffsetDateTime fechaFin;

    @Column(name = "asignado_por")
    private String asignadoPor;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @Column(name = "actualizado_por")
    private String actualizadoPor;

    @Column(name = "inactivado_por")
    private String inactivadoPor;

    @Column(name = "fecha_inactivacion")
    private OffsetDateTime fechaInactivacion;

    @Column(name = "eliminado_por")
    private String eliminadoPor;

    @Column(name = "fecha_eliminacion")
    private OffsetDateTime fechaEliminacion;

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

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
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

    public OffsetDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(OffsetDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
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

    public String getEliminadoPor() {
        return eliminadoPor;
    }

    public void setEliminadoPor(String eliminadoPor) {
        this.eliminadoPor = eliminadoPor;
    }

    public OffsetDateTime getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(OffsetDateTime fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }
}
