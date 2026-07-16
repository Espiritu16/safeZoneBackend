package com.utp.safezonebackend.denuncias.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TipoViolenciaNormalizerTest {

    @Test
    void normalizaVariantesAlCodigoOficial() {
        assertThat(TipoViolenciaNormalizer.normalizar("Física")).isEqualTo("FISICA");
        assertThat(TipoViolenciaNormalizer.normalizar("Violencia Física")).isEqualTo("FISICA");
        assertThat(TipoViolenciaNormalizer.normalizar("FISICA")).isEqualTo("FISICA");
        assertThat(TipoViolenciaNormalizer.normalizar("Psicológica")).isEqualTo("PSICOLOGICA");
        assertThat(TipoViolenciaNormalizer.normalizar("Violencia Psicológica")).isEqualTo("PSICOLOGICA");
        assertThat(TipoViolenciaNormalizer.normalizar("Violencia Económica")).isEqualTo("ECONOMICA");
    }

    @Test
    void etiquetaDevuelveTextoVisibleUnificado() {
        assertThat(TipoViolenciaNormalizer.etiqueta("VIOLENCIA PSICOLOGICA")).isEqualTo("Psicológica");
        assertThat(TipoViolenciaNormalizer.etiqueta("FISICA")).isEqualTo("Física");
    }
}
