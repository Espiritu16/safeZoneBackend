package com.utp.safezonebackend.predenuncias.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record CrearPreDenunciaRequest(
        @Size(max = 120) String nombresContacto,
        @Size(max = 120) String apellidosContacto,
        @Size(max = 30) String telefonoContacto,
        @Email @Size(max = 255) String correoContacto,
        @NotBlank @Size(max = 4000) String descripcionHecho,
        @Size(max = 100) String tipoViolencia,
        OffsetDateTime fechaIncidente,
        @Size(max = 120) String distrito,
        @Size(max = 255) String direccionReferencia,
        Boolean anonima
) {
}
