package com.utp.safezonebackend.victimas.dto.response;

import com.utp.safezonebackend.citas.dto.response.CitaResponse;
import com.utp.safezonebackend.denuncias.dto.response.DenunciaResponse;
import com.utp.safezonebackend.evidencias.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.seguimientos.dto.response.SeguimientoCasoResponse;

import java.util.List;

public class VictimaHistorialResponse {
    private String victimaId;
    private List<DenunciaResponse> denuncias;
    private List<CitaResponse> citas;
    private List<SeguimientoCasoResponse> seguimientos;
    private List<EvidenciaResponse> evidencias;

    public String getVictimaId() {
        return victimaId;
    }

    public void setVictimaId(String victimaId) {
        this.victimaId = victimaId;
    }

    public List<DenunciaResponse> getDenuncias() {
        return denuncias;
    }

    public void setDenuncias(List<DenunciaResponse> denuncias) {
        this.denuncias = denuncias;
    }

    public List<CitaResponse> getCitas() {
        return citas;
    }

    public void setCitas(List<CitaResponse> citas) {
        this.citas = citas;
    }

    public List<SeguimientoCasoResponse> getSeguimientos() {
        return seguimientos;
    }

    public void setSeguimientos(List<SeguimientoCasoResponse> seguimientos) {
        this.seguimientos = seguimientos;
    }

    public List<EvidenciaResponse> getEvidencias() {
        return evidencias;
    }

    public void setEvidencias(List<EvidenciaResponse> evidencias) {
        this.evidencias = evidencias;
    }
}
