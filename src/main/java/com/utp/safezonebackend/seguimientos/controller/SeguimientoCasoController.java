package com.utp.safezonebackend.seguimientos.controller;

import com.utp.safezonebackend.seguimientos.dto.request.CrearSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.dto.request.ActualizarSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.dto.response.SeguimientoCasoResponse;
import com.utp.safezonebackend.seguimientos.service.SeguimientoCasoService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "SeguimientoCaso", description = "Seguimiento de avances de casos")
@RestController
@RequestMapping("/api/seguimientos")
public class SeguimientoCasoController {

    private final SeguimientoCasoService service;

    public SeguimientoCasoController(SeguimientoCasoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar seguimientos de caso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<SeguimientoCasoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener seguimiento de caso por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/inactivar")
    public ResponseEntity<SeguimientoCasoResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear seguimiento de caso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity crear(@RequestBody CrearSeguimientoCasoRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @Operation(summary = "Actualizar seguimiento de caso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/inactivar")
    public ResponseEntity actualizar(@PathVariable String id, @RequestBody ActualizarSeguimientoCasoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Eliminar seguimiento de caso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sin contenido"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<Void> inactivar(@PathVariable String id) {
        service.inactivar(id);
        return ResponseEntity.noContent().build();
    }
}
