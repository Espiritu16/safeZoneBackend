package com.utp.safezonebackend.auditoria.dto.request;

import com.utp.safezonebackend.auditoria.enums.ResultadoAuditoria;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import java.util.Map;

public record RegistroAuditoriaInterna(
        String entidadTipo,
        String actorId,
        RolUsuario rolActor,
        String accion,
        String entidadId,
        ResultadoAuditoria resultado,
        String detalle,
        Map<String, Object> antes,
        Map<String, Object> despues,
        String ip,
        String agenteUsuario,
        String codigoSolicitud
) {
}
