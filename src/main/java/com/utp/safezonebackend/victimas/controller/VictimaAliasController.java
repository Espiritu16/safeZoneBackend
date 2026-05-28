package com.utp.safezonebackend.victimas.controller;

import com.utp.safezonebackend.victimas.dto.request.CrearVictimaAliasRequest;
import com.utp.safezonebackend.victimas.dto.request.InhabilitarVictimaAliasRequest;
import com.utp.safezonebackend.victimas.dto.request.ActualizarVictimaAliasRequest;
import com.utp.safezonebackend.victimas.dto.response.VictimaAliasResponse;
import com.utp.safezonebackend.victimas.service.VictimaAliasService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

@Tag(name = "VictimaAlias", description = "Gestion de alias anonimos de victimas")
@RestController
@RequestMapping("/api/victimasalias")
public class VictimaAliasController {

    private final VictimaAliasService service;

    public VictimaAliasController(VictimaAliasService service) {
        this.service = service;
    }


    @Operation(summary = "Obtener alias de victima por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VictimaAliasResponse> findById(
            @PathVariable String id,
            Authentication auth) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear alias de victima")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity crear(@RequestBody @Valid CrearVictimaAliasRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @Operation(summary = "Actualizar alias de victima")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/actualizar")
    public ResponseEntity actualizar(@PathVariable String id, @RequestBody ActualizarVictimaAliasRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Eliminar alias de victima")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sin contenido"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<Void> inactivar(@PathVariable String id, @RequestBody InhabilitarVictimaAliasRequest request) {
        service.inactivar(id,request);
        return ResponseEntity.noContent().build();
    }
}
