package com.utp.safezonebackend.shared.util;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public final class DistritoNormalizer {

    private static final Map<String, String> ALIAS = Map.ofEntries(
            Map.entry("lima", "Lima"),
            Map.entry("cercado de lima", "Lima"),
            Map.entry("lima cercado", "Lima"),
            Map.entry("los olivos", "Los Olivos"),
            Map.entry("losolivos", "Los Olivos"),
            Map.entry("villa el salvador", "Villa El Salvador"),
            Map.entry("ves", "Villa El Salvador"),
            Map.entry("san juan de lurigancho", "San Juan de Lurigancho"),
            Map.entry("sjl", "San Juan de Lurigancho"),
            Map.entry("san juan de miraflores", "San Juan de Miraflores"),
            Map.entry("sjm", "San Juan de Miraflores"),
            Map.entry("santa anita", "Santa Anita"),
            Map.entry("santa fe", "Santa Fe"),
            Map.entry("lima norte", "Lima Norte"),
            Map.entry("lima sur", "Lima Sur"),
            Map.entry("lima este", "Lima Este"),
            Map.entry("lima centro", "Lima Centro"),
            Map.entry("comas", "Comas")
    );

    private DistritoNormalizer() {
    }

    public static String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String limpio = compactarEspacios(valor);
        String clave = sinTildes(limpio).toLowerCase(Locale.ROOT);
        return ALIAS.getOrDefault(clave, titulo(limpio));
    }

    public static String etiqueta(String valor) {
        String normalizado = normalizar(valor);
        return normalizado == null ? "Sin distrito" : normalizado;
    }

    private static String compactarEspacios(String valor) {
        return valor.trim().replaceAll("\\s+", " ");
    }

    private static String sinTildes(String valor) {
        String normalizado = Normalizer.normalize(valor, Normalizer.Form.NFD);
        return normalizado.replaceAll("\\p{M}", "");
    }

    private static String titulo(String valor) {
        return Arrays.stream(valor.toLowerCase(Locale.ROOT).split(" "))
                .filter(part -> !part.isBlank())
                .map(DistritoNormalizer::capitalizar)
                .collect(Collectors.joining(" "));
    }

    private static String capitalizar(String valor) {
        if (valor.isBlank()) {
            return valor;
        }
        return valor.substring(0, 1).toUpperCase(Locale.ROOT) + valor.substring(1);
    }
}
