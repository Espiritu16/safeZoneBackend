package com.utp.safezonebackend.evidencias.dto.request;

public record VincularEvidenciaRequest(
        String casoId,
        String denunciaId,
        String predenunciaId
) {}
