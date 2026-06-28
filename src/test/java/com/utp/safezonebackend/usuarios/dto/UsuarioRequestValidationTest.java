package com.utp.safezonebackend.usuarios.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.utp.safezonebackend.usuarios.dto.request.ActualizarUsuarioRequest;
import com.utp.safezonebackend.usuarios.dto.request.CrearUsuarioRequest;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UsuarioRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void crearUsuarioRechazaDniTelefonoYContrasenaInvalidos() {
        CrearUsuarioRequest request = new CrearUsuarioRequest(
                "usuario@safezone.gob.pe",
                "clave",
                "Maria",
                "Torres",
                "1234A678",
                "999",
                "Comas",
                RolUsuario.RECEPCIONISTA
        );

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("contrasena", "dni", "telefono");
    }

    @Test
    void actualizarUsuarioRechazaCamposOpcionalesConFormatoInvalido() {
        ActualizarUsuarioRequest request = new ActualizarUsuarioRequest(
                "correo-invalido",
                "",
                "A".repeat(121),
                "1234567X",
                "123",
                "D".repeat(121),
                RolUsuario.PSICOLOGO,
                true
        );

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("correo", "nombres", "apellidos", "dni", "telefono", "distrito");
    }
}
