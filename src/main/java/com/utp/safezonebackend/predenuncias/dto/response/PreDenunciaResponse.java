package com.utp.safezonebackend.predenuncias.dto.response;

import com.utp.safezonebackend.predenuncias.enums.EstadoPreDenuncia;
import java.time.OffsetDateTime;

public record PreDenunciaResponse(
        String id,
        String nombresContacto,
        String apellidosContacto,
        String telefonoContacto,
        String correoContacto,
        String descripcionHecho,
        String tipoViolencia,
        OffsetDateTime fechaIncidente,
        String distrito,
        String direccionReferencia,
        boolean anonima,
        EstadoPreDenuncia estado,
        String motivoDescarte,
        String victimaId,
        String denunciaId,
        String casoId,
        String asignadaA,
        OffsetDateTime fechaContacto,
        OffsetDateTime fechaFormalizacion,
        OffsetDateTime fechaCreacion,
        OffsetDateTime fechaActualizacion
) {
}
