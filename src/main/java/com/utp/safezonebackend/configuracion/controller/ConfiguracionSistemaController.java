package com.utp.safezonebackend.configuracion.controller;

import com.utp.safezonebackend.configuracion.dto.request.CreateConfiguracionSistemaRequest;
import com.utp.safezonebackend.configuracion.dto.request.UpdateConfiguracionSistemaRequest;
import com.utp.safezonebackend.configuracion.dto.response.ConfiguracionSistemaResponse;
import com.utp.safezonebackend.configuracion.service.ConfiguracionSeguridadService;
import com.utp.safezonebackend.configuracion.service.ConfiguracionSistemaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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

@Tag(name = "ConfiguracionSistema", description = "Parametros de configuracion del sistema")
@RestController
@RequestMapping("/api/configuracion")
public class ConfiguracionSistemaController {

    private final ConfiguracionSistemaService service;
    private final ConfiguracionSeguridadService seguridadService;

    public ConfiguracionSistemaController(ConfiguracionSistemaService service, ConfiguracionSeguridadService seguridadService) {
        this.service = service;
        this.seguridadService = seguridadService;
    }

    @Operation(summary = "Listar configuraciones del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ConfiguracionSistemaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener criterios de clasificacion de riesgo")
    @GetMapping("/seguridad/criterios-riesgo")
    public ResponseEntity<Map<String, Integer>> obtenerCriteriosRiesgo() {
        return ResponseEntity.ok(seguridadService.obtenerCriteriosClasificacionRiesgo());
    }

    @Operation(summary = "Obtener configuracion del sistema por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracionSistemaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear configuracion del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ConfiguracionSistemaResponse> create(@Valid @RequestBody CreateConfiguracionSistemaRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @Operation(summary = "Actualizar configuracion del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracionSistemaResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateConfiguracionSistemaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Inactivar configuracion del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sin contenido"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<Void> inactivar(@PathVariable Long id) {
        service.inactivar(id);
        return ResponseEntity.noContent().build();
    }
}
