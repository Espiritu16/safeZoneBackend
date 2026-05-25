package com.utp.safezonebackend.victimas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "victimas_alias")
public class VictimaAlias {

    @Id
    private String id;

    @Column(name = "victima_id")
    private String victimaId;

    @Column(name = "alias_codigo")
    private String aliasCodigo;

    private boolean activo;

    @Column(name = "fecha_asignacion")
    private OffsetDateTime fechaAsignacion;

    @Column(name = "fecha_fin")
    private OffsetDateTime fechaFin;

    @Column(name = "creado_por")
    private String creadoPor;

    @Column(name = "actualizado_por")
    private String actualizadoPor;

    @Column(name = "inactivado_por")
    private String inactivadoPor;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @Column(name = "fecha_inactivacion")
    private OffsetDateTime fechaInactivacion;
}
