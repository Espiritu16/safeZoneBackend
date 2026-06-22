package com.utp.safezonebackend.panel.dto.response;

import java.util.List;

public record PanelRolResponse(
        String usuarioId,
        String nombre,
        String rol,
        List<String> modulos,
        List<String> permisos,
        List<Indicador> indicadores,
        List<AccionRapida> acciones,
        List<Alerta> alertas
) {
    public record Indicador(String clave, String etiqueta, long valor) {
    }

    public record AccionRapida(String clave, String etiqueta, String modulo) {
    }

    public record Alerta(String tipo, String mensaje, String severidad) {
    }
}
