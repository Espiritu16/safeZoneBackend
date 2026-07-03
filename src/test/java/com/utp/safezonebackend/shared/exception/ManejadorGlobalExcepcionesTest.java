package com.utp.safezonebackend.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.utp.safezonebackend.auth.dto.response.RespuestaBasica;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

class ManejadorGlobalExcepcionesTest {

    @Test
    void manejarAutenticacionDevuelveUnauthorized() {
        ManejadorGlobalExcepciones manejador = new ManejadorGlobalExcepciones();

        ResponseEntity<RespuestaBasica> response = manejador.manejarAutenticacion(
                new ExcepcionAutenticacion("Correo o contraseña incorrectos.")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().success()).isFalse();
        assertThat(response.getBody().message()).isEqualTo("Correo o contraseña incorrectos.");
    }

    @Test
    void manejarJsonInvalidoDevuelveBadRequest() {
        ManejadorGlobalExcepciones manejador = new ManejadorGlobalExcepciones();

        ResponseEntity<RespuestaBasica> response = manejador.manejarJsonInvalido(
                new HttpMessageNotReadableException("Fecha invalida")
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().success()).isFalse();
        assertThat(response.getBody().message()).contains("Formato de solicitud invalido");
    }
}
