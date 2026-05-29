package com.utp.safezonebackend.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    private String id;

    @Column(name = "usuario_id")
    private String usuarioId;

    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "expira_en")
    private OffsetDateTime expiraEn;

    private boolean revocado;

    @Column(name = "revocado_por")
    private String revocadoPor;

    @Column(name = "fecha_revocacion")
    private OffsetDateTime fechaRevocacion;

    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    private boolean activo;

    public RefreshToken() {
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

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public OffsetDateTime getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(OffsetDateTime expiraEn) {
        this.expiraEn = expiraEn;
    }

    public boolean isRevocado() {
        return revocado;
    }

    public void setRevocado(boolean revocado) {
        this.revocado = revocado;
    }

    public String getRevocadoPor() {
        return revocadoPor;
    }

    public void setRevocadoPor(String revocadoPor) {
        this.revocadoPor = revocadoPor;
    }

    public OffsetDateTime getFechaRevocacion() {
        return fechaRevocacion;
    }

    public void setFechaRevocacion(OffsetDateTime fechaRevocacion) {
        this.fechaRevocacion = fechaRevocacion;
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
}
