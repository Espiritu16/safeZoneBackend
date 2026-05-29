package com.utp.safezonebackend.predenuncias.entity;

import com.utp.safezonebackend.predenuncias.enums.EstadoPreDenuncia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "pre_denuncias")
public class PreDenuncia {

    @Id
    private String id;

    @Column(name = "nombres_contacto")
    private String nombresContacto;

    @Column(name = "apellidos_contacto")
    private String apellidosContacto;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    @Column(name = "correo_contacto")
    private String correoContacto;

    @Column(name = "descripcion_hecho")
    private String descripcionHecho;

    @Column(name = "tipo_violencia")
    private String tipoViolencia;

    @Column(name = "fecha_incidente")
    private OffsetDateTime fechaIncidente;

    private String distrito;

    @Column(name = "direccion_referencia")
    private String direccionReferencia;

    @Column(name = "es_anonima")
    private boolean anonima;

    @Enumerated(EnumType.STRING)
    private EstadoPreDenuncia estado;

    @Column(name = "motivo_descarte")
    private String motivoDescarte;

    @Column(name = "victima_id")
    private String victimaId;

    @Column(name = "denuncia_id")
    private String denunciaId;

    @Column(name = "caso_id")
    private String casoId;

    @Column(name = "asignada_a")
    private String asignadaA;

    @Column(name = "fecha_contacto")
    private OffsetDateTime fechaContacto;

    @Column(name = "fecha_formalizacion")
    private OffsetDateTime fechaFormalizacion;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombresContacto() {
        return nombresContacto;
    }

    public void setNombresContacto(String nombresContacto) {
        this.nombresContacto = nombresContacto;
    }

    public String getApellidosContacto() {
        return apellidosContacto;
    }

    public void setApellidosContacto(String apellidosContacto) {
        this.apellidosContacto = apellidosContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public String getDescripcionHecho() {
        return descripcionHecho;
    }

    public void setDescripcionHecho(String descripcionHecho) {
        this.descripcionHecho = descripcionHecho;
    }

    public String getTipoViolencia() {
        return tipoViolencia;
    }

    public void setTipoViolencia(String tipoViolencia) {
        this.tipoViolencia = tipoViolencia;
    }

    public OffsetDateTime getFechaIncidente() {
        return fechaIncidente;
    }

    public void setFechaIncidente(OffsetDateTime fechaIncidente) {
        this.fechaIncidente = fechaIncidente;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDireccionReferencia() {
        return direccionReferencia;
    }

    public void setDireccionReferencia(String direccionReferencia) {
        this.direccionReferencia = direccionReferencia;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public void setAnonima(boolean anonima) {
        this.anonima = anonima;
    }

    public EstadoPreDenuncia getEstado() {
        return estado;
    }

    public void setEstado(EstadoPreDenuncia estado) {
        this.estado = estado;
    }

    public String getMotivoDescarte() {
        return motivoDescarte;
    }

    public void setMotivoDescarte(String motivoDescarte) {
        this.motivoDescarte = motivoDescarte;
    }

    public String getVictimaId() {
        return victimaId;
    }

    public void setVictimaId(String victimaId) {
        this.victimaId = victimaId;
    }

    public String getDenunciaId() {
        return denunciaId;
    }

    public void setDenunciaId(String denunciaId) {
        this.denunciaId = denunciaId;
    }

    public String getCasoId() {
        return casoId;
    }

    public void setCasoId(String casoId) {
        this.casoId = casoId;
    }

    public String getAsignadaA() {
        return asignadaA;
    }

    public void setAsignadaA(String asignadaA) {
        this.asignadaA = asignadaA;
    }

    public OffsetDateTime getFechaContacto() {
        return fechaContacto;
    }

    public void setFechaContacto(OffsetDateTime fechaContacto) {
        this.fechaContacto = fechaContacto;
    }

    public OffsetDateTime getFechaFormalizacion() {
        return fechaFormalizacion;
    }

    public void setFechaFormalizacion(OffsetDateTime fechaFormalizacion) {
        this.fechaFormalizacion = fechaFormalizacion;
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
