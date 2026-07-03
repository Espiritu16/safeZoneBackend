package com.utp.safezonebackend.victimas.controller;

import com.utp.safezonebackend.victimas.dto.response.VictimaHistorialResponse;
import com.utp.safezonebackend.victimas.service.VictimaHistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Victima Historial", description = "Historial consolidado de atención a la víctima")
@RestController
@RequestMapping("/api/victimas")
public class VictimaHistorialController {

    private final VictimaHistorialService service;

    public VictimaHistorialController(VictimaHistorialService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener mi historial consolidado como víctima autenticada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado"),
            @ApiResponse(responseCode = "403", description = "Rol no autorizado")
    })
    @GetMapping("/me/historial")
    public ResponseEntity<VictimaHistorialResponse> obtenerMiHistorial() {
        return ResponseEntity.ok(service.obtenerHistorialAutenticado());
    }

    @Operation(summary = "Obtener historial consolidado de una víctima")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Víctima no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/historial")
    public ResponseEntity<VictimaHistorialResponse> obtenerHistorial(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(service.obtenerHistorialPorVictima(id));
    }

    @Operation(summary = "Obtener historial consolidado mediante alias anonimo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "404", description = "Alias no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/alias/{aliasCodigo}/historial")
    public ResponseEntity<VictimaHistorialResponse> obtenerHistorialPorAlias(@PathVariable String aliasCodigo) {
        return ResponseEntity.ok(service.obtenerPorAlias(aliasCodigo));
    }
}
