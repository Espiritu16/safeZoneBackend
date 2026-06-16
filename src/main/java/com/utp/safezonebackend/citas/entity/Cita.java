package com.utp.safezonebackend.citas.entity;

import com.utp.safezonebackend.citas.enums.EstadoCita;
import com.utp.safezonebackend.citas.enums.TipoCita;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

import lombok.Data;

@Data
@Entity
@Table(name = "cita")
public class Cita {

    @Id
    private String id;

    @Column(name = "caso_id")
    private String casoId;

    @Column(name = "victima_id")
    private String victimaId;

    @Column(name = "especialista_id")
    private String especialistaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cita")
    private TipoCita tipoCita;

    @Column(name = "fecha_inicio")
    private OffsetDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private OffsetDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    @Column(name = "motivo_cancelacion")
    private String motivoCancelacion;

    private String observaciones;

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
}
