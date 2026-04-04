package com.utp.safezonebackend.entity;

import com.utp.safezonebackend.entity.enums.RolUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "seguimientos_caso")
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

    private boolean eliminado;

    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

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

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public OffsetDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(OffsetDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
