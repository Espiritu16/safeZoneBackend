package com.utp.safezonebackend.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.utp.safezonebackend.entity.enums.ResultadoAuditoria;
import com.utp.safezonebackend.entity.enums.RolUsuario;
import java.time.OffsetDateTime;
import java.util.Map;
import org.springframework.data.annotation.Id;

@Container(containerName = "auditoria")
public class Auditoria {

    @Id
    private String id;

    @PartitionKey
    private String entidadTipo;

    private OffsetDateTime fecha;
    private String actorId;
    private RolUsuario actorRol;
    private String accion;
    private String entidadId;
    private ResultadoAuditoria resultado;
    private String detalle;
    private Map<String, Object> antes;
    private Map<String, Object> despues;
    private String ip;
    private String userAgent;
    private String requestId;

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
}
