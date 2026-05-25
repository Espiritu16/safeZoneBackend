package com.utp.safezonebackend.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "refresh_tokens")
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
}
