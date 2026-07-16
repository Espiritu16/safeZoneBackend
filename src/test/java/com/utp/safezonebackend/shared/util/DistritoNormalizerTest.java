package com.utp.safezonebackend.shared.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DistritoNormalizerTest {

    @Test
    void normalizarAgrupaAliasesYFormateaDistritos() {
        assertThat(DistritoNormalizer.normalizar(" los  olivos ")).isEqualTo("Los Olivos");
        assertThat(DistritoNormalizer.normalizar("Lima Cercado")).isEqualTo("Lima");
        assertThat(DistritoNormalizer.normalizar("cercado de lima")).isEqualTo("Lima");
        assertThat(DistritoNormalizer.normalizar("santa fe")).isEqualTo("Santa Fe");
        assertThat(DistritoNormalizer.normalizar("VILLA EL SALVADOR")).isEqualTo("Villa El Salvador");
        assertThat(DistritoNormalizer.normalizar("distrito personalizado")).isEqualTo("Distrito Personalizado");
    }

    @Test
    void etiquetaDevuelveSinDistritoCuandoNoHayValor() {
        assertThat(DistritoNormalizer.etiqueta(null)).isEqualTo("Sin distrito");
        assertThat(DistritoNormalizer.etiqueta("  ")).isEqualTo("Sin distrito");
    }
}
