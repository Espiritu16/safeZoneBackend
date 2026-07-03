package com.utp.safezonebackend.evidencias.dto.response;

public record EvidenciaResponse(
        String id,
        String url,
        String nombreOriginal,
        long tamano,
        String subidoPor,
        String fechaCreacion,
        String casoId,
        String denunciaId,
        String predenunciaId
) {}
