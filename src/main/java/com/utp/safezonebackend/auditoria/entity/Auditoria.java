package com.utp.safezonebackend.auditoria.entity;

import com.utp.safezonebackend.auditoria.enums.ResultadoAuditoria;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    private String id;

    @Column(name = "entidad_tipo")
    private String entidadTipo;

    @Column(name = "fecha_evento")
    private OffsetDateTime fecha;

    @Column(name = "actor_id")
    private String actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_actor")
    private RolUsuario actorRol;

    private String accion;

    @Column(name = "entidad_id")
    private String entidadId;

    @Enumerated(EnumType.STRING)
    private ResultadoAuditoria resultado;

    private String detalle;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> antes;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> despues;

    private String ip;

    @Column(name = "agente_usuario")
    private String userAgent;

    @Column(name = "codigo_solicitud")
    private String requestId;

    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    public Auditoria() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntidadTipo() {
        return entidadTipo;
    }

    public void setEntidadTipo(String entidadTipo) {
        this.entidadTipo = entidadTipo;
    }

    public OffsetDateTime getFecha() {
        return fecha;
    }

    public void setFecha(OffsetDateTime fecha) {
        this.fecha = fecha;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public RolUsuario getActorRol() {
        return actorRol;
    }

    public void setActorRol(RolUsuario actorRol) {
        this.actorRol = actorRol;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(String entidadId) {
        this.entidadId = entidadId;
    }

    public ResultadoAuditoria getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoAuditoria resultado) {
        this.resultado = resultado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Map<String, Object> getAntes() {
        return antes;
    }

    public void setAntes(Map<String, Object> antes) {
        this.antes = antes;
    }

    public Map<String, Object> getDespues() {
        return despues;
    }

    public void setDespues(Map<String, Object> despues) {
        this.despues = despues;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
