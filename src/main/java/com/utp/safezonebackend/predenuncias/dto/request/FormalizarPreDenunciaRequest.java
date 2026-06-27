package com.utp.safezonebackend.predenuncias.dto.request;

import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;

public record FormalizarPreDenunciaRequest(
        String victimaId,
        String denunciaId,
        String casoId,
        NivelRiesgo nivelRiesgo,
        Boolean formalizarAnonima,
        Integer edad
) {
}
