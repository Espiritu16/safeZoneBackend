package com.utp.safezonebackend.evidencias.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

import lombok.Data;

@Data
@Entity
@Table(name = "evidencia")
public class Evidencia {

    @Id
    private String id;

    @Column(name = "caso_id")
    private String casoId;

    @Column(name = "denuncia_id")
    private String denunciaId;

    @Column(name = "seguimiento_id")
    private String seguimientoId;

    @Column(name = "subido_por")
    private String subidoPor;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "tipo_mime")
    private String tipoMime;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "url_almacenamiento")
    private String urlAlmacenamiento;

    @Column(name = "hash_sha256")
    private String hashSha256;

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

    public String getDenunciaId() {
        return denunciaId;
    }

    public void setDenunciaId(String denunciaId) {
        this.denunciaId = denunciaId;
    }

    public String getSeguimientoId() {
        return seguimientoId;
    }

    public void setSeguimientoId(String seguimientoId) {
        this.seguimientoId = seguimientoId;
    }

    public String getSubidoPor() {
        return subidoPor;
    }

    public void setSubidoPor(String subidoPor) {
        this.subidoPor = subidoPor;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getTipoMime() {
        return tipoMime;
    }

    public void setTipoMime(String tipoMime) {
        this.tipoMime = tipoMime;
    }

    public Long getTamanoBytes() {
        return tamanoBytes;
    }

    public void setTamanoBytes(Long tamanoBytes) {
        this.tamanoBytes = tamanoBytes;
    }

    public String getUrlAlmacenamiento() {
        return urlAlmacenamiento;
    }

    public void setUrlAlmacenamiento(String urlAlmacenamiento) {
        this.urlAlmacenamiento = urlAlmacenamiento;
    }

    public String getHashSha256() {
        return hashSha256;
    }

    public void setHashSha256(String hashSha256) {
        this.hashSha256 = hashSha256;
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

    public void setFechaInactivacion(OffsetDateTime fechaInactivacion) {
        this.fechaInactivacion = fechaInactivacion;
    }
}
