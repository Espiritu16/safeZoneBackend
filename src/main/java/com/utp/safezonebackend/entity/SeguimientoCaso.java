package com.utp.safezonebackend.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.utp.safezonebackend.entity.enums.RolUsuario;
import java.time.OffsetDateTime;
import org.springframework.data.annotation.Id;

@Container(containerName = "seguimientos_caso")
public class SeguimientoCaso {

    @Id
    private String id;

    @PartitionKey
    private String casoId;

    private String autorId;
    private RolUsuario rolAutor;
    private String tipoSeguimiento;
    private String contenido;
    private String proximaAccion;
    private OffsetDateTime fechaProximaAccion;
    private OffsetDateTime fechaCreacion;

    public SeguimientoCaso() {
    }

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

    public String getAutorId() {
        return autorId;
    }

    public void setAutorId(String autorId) {
        this.autorId = autorId;
    }

    public RolUsuario getRolAutor() {
        return rolAutor;
    }

    public void setRolAutor(RolUsuario rolAutor) {
        this.rolAutor = rolAutor;
    }

    public String getTipoSeguimiento() {
        return tipoSeguimiento;
    }

    public void setTipoSeguimiento(String tipoSeguimiento) {
        this.tipoSeguimiento = tipoSeguimiento;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getProximaAccion() {
        return proximaAccion;
    }

    public void setProximaAccion(String proximaAccion) {
        this.proximaAccion = proximaAccion;
    }

    public OffsetDateTime getFechaProximaAccion() {
        return fechaProximaAccion;
    }

    public void setFechaProximaAccion(OffsetDateTime fechaProximaAccion) {
        this.fechaProximaAccion = fechaProximaAccion;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
