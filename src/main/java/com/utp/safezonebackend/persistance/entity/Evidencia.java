package com.utp.safezonebackend.persistance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "evidencias")
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
