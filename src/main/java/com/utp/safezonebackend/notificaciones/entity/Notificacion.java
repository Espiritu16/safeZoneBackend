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
@Table(name = "notificaciones")
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

    private boolean eliminado;

    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private OffsetDateTime fechaActualizacion;

    @Column(name = "fecha_eliminacion")
    private OffsetDateTime fechaEliminacion;

    @Column(name = "creado_por")
    private String creadoPor;

    @Column(name = "actualizado_por")
    private String actualizadoPor;

    @Column(name = "eliminado_por")
    private String eliminadoPor;
}
