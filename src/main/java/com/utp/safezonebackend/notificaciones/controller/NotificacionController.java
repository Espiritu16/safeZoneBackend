package com.utp.safezonebackend.notificaciones.controller;

import com.utp.safezonebackend.notificaciones.dto.request.CrearNotificacionRequest;
import com.utp.safezonebackend.notificaciones.dto.request.ActualizarNotificacionRequest;
import com.utp.safezonebackend.notificaciones.dto.response.NotificacionResponse;
import com.utp.safezonebackend.notificaciones.service.NotificacionService;
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

@Tag(name = "Notificacion", description = "Gestion de notificaciones")
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService service;

    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @Operation(summary = "Listar notificaciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<NotificacionResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener notificacion por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/inactivar")
    public ResponseEntity<NotificacionResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear notificacion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity crear(@RequestBody CrearNotificacionRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @Operation(summary = "Actualizar notificacion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/inactivar")
    public ResponseEntity actualizar(@PathVariable String id, @RequestBody ActualizarNotificacionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Eliminar notificacion")
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
