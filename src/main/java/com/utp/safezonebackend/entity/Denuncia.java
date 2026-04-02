package com.utp.safezonebackend.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.utp.safezonebackend.entity.enums.NivelRiesgo;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;

@Container(containerName = "denuncias")
public class Denuncia {

    @Id
    private String id;

    @PartitionKey
    private String casoId;

    private String victimaId;
    private String descripcion;
    private String tipoViolencia;
    private OffsetDateTime fechaIncidente;
    private String distrito;
    private String direccionReferencia;
    private NivelRiesgo nivelRiesgo;
    private boolean anonima;
    private List<String> adjuntos;
    private OffsetDateTime fechaCreacion;

    public Denuncia() {
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

    public String getVictimaId() {
        return victimaId;
    }

    public void setVictimaId(String victimaId) {
        this.victimaId = victimaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoViolencia() {
        return tipoViolencia;
    }

    public void setTipoViolencia(String tipoViolencia) {
        this.tipoViolencia = tipoViolencia;
    }

    public OffsetDateTime getFechaIncidente() {
        return fechaIncidente;
    }

    public void setFechaIncidente(OffsetDateTime fechaIncidente) {
        this.fechaIncidente = fechaIncidente;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDireccionReferencia() {
        return direccionReferencia;
    }

    public void setDireccionReferencia(String direccionReferencia) {
        this.direccionReferencia = direccionReferencia;
    }

    public NivelRiesgo getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(NivelRiesgo nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public void setAnonima(boolean anonima) {
        this.anonima = anonima;
    }

    public List<String> getAdjuntos() {
        return adjuntos;
    }

    public void setAdjuntos(List<String> adjuntos) {
        this.adjuntos = adjuntos;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
