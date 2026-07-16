package com.utp.safezonebackend.denuncias.util;

import java.text.Normalizer;
import java.util.Locale;

public final class TipoViolenciaNormalizer {

    private TipoViolenciaNormalizer() {
    }

    public static String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String normalizado = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toUpperCase(Locale.ROOT)
                .replace('-', ' ')
                .replace('_', ' ');
        normalizado = normalizado.replaceAll("\\s+", " ");
        if (normalizado.startsWith("VIOLENCIA ")) {
            normalizado = normalizado.substring("VIOLENCIA ".length()).trim();
        }
        return switch (normalizado) {
            case "FISICA" -> "FISICA";
            case "PSICOLOGICA", "PSICOLOGICA EMOCIONAL", "EMOCIONAL" -> "PSICOLOGICA";
            case "SEXUAL" -> "SEXUAL";
            case "ECONOMICA" -> "ECONOMICA";
            case "PATRIMONIAL" -> "PATRIMONIAL";
            case "DIGITAL", "CIBERNETICA", "VIRTUAL" -> "DIGITAL";
            default -> "OTRA";
        };
    }

    public static String etiqueta(String valor) {
        return switch (normalizar(valor)) {
            case "FISICA" -> "Física";
            case "PSICOLOGICA" -> "Psicológica";
            case "SEXUAL" -> "Sexual";
            case "ECONOMICA" -> "Económica";
            case "PATRIMONIAL" -> "Patrimonial";
            case "DIGITAL" -> "Digital";
            case null, default -> "Otra";
        };
    }
}
