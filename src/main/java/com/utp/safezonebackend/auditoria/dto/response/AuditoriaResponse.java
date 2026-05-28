package com.utp.safezonebackend.auditoria.dto.response;

import java.time.OffsetDateTime;
import java.util.Map;

public record AuditoriaResponse(
        String id,
        String entidadTipo,
        OffsetDateTime fechaEvento,
        String actorId,
        String rolActor,
        String accion,
        String entidadId,
        String resultado,
        String detalle,
        Map<String, Object> antes,
        Map<String, Object> despues,
        String ip,
        String agenteUsuario,
        String codigoSolicitud
) {
}
