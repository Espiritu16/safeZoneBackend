package com.utp.safezonebackend.evidencias.controller;

import com.utp.safezonebackend.evidencias.dto.request.CreateEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.request.UpdateEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.evidencias.service.EvidenciaService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@Tag(name = "Evidencia", description = "Gestion de evidencias de casos")
@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {

    private final EvidenciaService service;

    public EvidenciaController(EvidenciaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar registros")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<EvidenciaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener registro por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EvidenciaResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<EvidenciaResponse> create(@RequestBody CreateEvidenciaRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @Operation(summary = "Actualizar registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EvidenciaResponse> update(@PathVariable String id, @RequestBody UpdateEvidenciaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Eliminar registro")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sin contenido"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
