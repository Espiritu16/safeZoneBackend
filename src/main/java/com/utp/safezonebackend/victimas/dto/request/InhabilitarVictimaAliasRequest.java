package com.utp.safezonebackend.victimas.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public class InhabilitarVictimaAliasRequest {
    @NotBlank
    private String inactivadoPor;
    public String getInactivadoPor() {
        return inactivadoPor;
    }
    public void setInactivadoPor(String inactivadoPor) {
        this.inactivadoPor = inactivadoPor;
    }
}
