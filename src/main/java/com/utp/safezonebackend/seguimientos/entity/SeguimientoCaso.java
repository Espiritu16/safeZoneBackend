package com.utp.safezonebackend.seguimientos.entity;

import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "seguimiento_caso")
public class SeguimientoCaso {

    @Id
    private String id;

    @Column(name = "caso_id")
    private String casoId;

    @Column(name = "autor_id")
    private String autorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_autor")
    private RolUsuario rolAutor;

    @Column(name = "tipo_seguimiento")
    private String tipoSeguimiento;

    private String contenido;

    @Column(name = "proxima_accion")
    private String proximaAccion;

    @Column(name = "fecha_proxima_accion")
    private OffsetDateTime fechaProximaAccion;

    private boolean activo;

    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

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

    public SeguimientoCaso() {
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

    public String getAutorId() {
        return autorId;
    }

    public void setAutorId(String autorId) {
        this.autorId = autorId;
    }

    public RolUsuario getRolAutor() {
        return rolAutor;
    }

    public void setRolAutor(RolUsuario rolAutor) {
        this.rolAutor = rolAutor;
    }

    public String getTipoSeguimiento() {
        return tipoSeguimiento;
    }

    public void setTipoSeguimiento(String tipoSeguimiento) {
        this.tipoSeguimiento = tipoSeguimiento;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getProximaAccion() {
        return proximaAccion;
    }

    public void setProximaAccion(String proximaAccion) {
        this.proximaAccion = proximaAccion;
    }

    public OffsetDateTime getFechaProximaAccion() {
        return fechaProximaAccion;
    }

    public void setFechaProximaAccion(OffsetDateTime fechaProximaAccion) {
        this.fechaProximaAccion = fechaProximaAccion;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
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
