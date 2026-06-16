package com.utp.safezonebackend.notificaciones.entity;

import com.utp.safezonebackend.notificaciones.enums.PrioridadNotificacion;
import com.utp.safezonebackend.notificaciones.enums.TipoNotificacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    private String id;

    @Column(name = "usuario_id")
    private String usuarioId;

    @Column(name = "caso_id")
    private String casoId;

    @Column(name = "denuncia_id")
    private String denunciaId;

    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;

    @Enumerated(EnumType.STRING)
    private PrioridadNotificacion prioridad;

    private String titulo;

    private String mensaje;

    private boolean leida;

    @Column(name = "fecha_lectura")
    private OffsetDateTime fechaLectura;

    private boolean activo;

    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @Column(name = "fecha_inactivacion")
    private OffsetDateTime fechaInactivacion;

    @Column(name = "creado_por")
    private String creadoPor;

    @Column(name = "actualizado_por")
    private String actualizadoPor;

    @Column(name = "inactivado_por")
    private String inactivadoPor;

    public Notificacion() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCasoId() {
        return casoId;
    }

    public void setCasoId(String casoId) {
        this.casoId = casoId;
    }

    public String getDenunciaId() {
        return denunciaId;
    }

    public void setDenunciaId(String denunciaId) {
        this.denunciaId = denunciaId;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public PrioridadNotificacion getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadNotificacion prioridad) {
        this.prioridad = prioridad;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public OffsetDateTime getFechaLectura() {
        return fechaLectura;
    }

    public void setFechaLectura(OffsetDateTime fechaLectura) {
        this.fechaLectura = fechaLectura;
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
}
