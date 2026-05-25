package com.utp.safezonebackend.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "recuperacion_contrasena_codigos")
public class RecuperacionContrasenaCodigo {

    @Id
    private String id;

    @Column(name = "usuario_id")
    private String usuarioId;

    @Column(name = "codigo_hash")
    private String codigoHash;

    @Column(name = "expira_en")
    private LocalDateTime expiraEn;

    private boolean usado;

    @Column(name = "fecha_uso")
    private LocalDateTime fechaUso;

    private int intentos;

    @Column(name = "max_intentos")
    private int maxIntentos;

    @Column(name = "solicitado_desde_ip")
    private String solicitadoDesdeIp;

    @Column(name = "agente_usuario")
    private String agenteUsuario;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    private boolean activo;

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

    public String getCodigoHash() {
        return codigoHash;
    }

    public void setCodigoHash(String codigoHash) {
        this.codigoHash = codigoHash;
    }

    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(LocalDateTime expiraEn) {
        this.expiraEn = expiraEn;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public LocalDateTime getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(LocalDateTime fechaUso) {
        this.fechaUso = fechaUso;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public int getMaxIntentos() {
        return maxIntentos;
    }

    public void setMaxIntentos(int maxIntentos) {
        this.maxIntentos = maxIntentos;
    }

    public String getSolicitadoDesdeIp() {
        return solicitadoDesdeIp;
    }

    public void setSolicitadoDesdeIp(String solicitadoDesdeIp) {
        this.solicitadoDesdeIp = solicitadoDesdeIp;
    }

    public String getAgenteUsuario() {
        return agenteUsuario;
    }

    public void setAgenteUsuario(String agenteUsuario) {
        this.agenteUsuario = agenteUsuario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
