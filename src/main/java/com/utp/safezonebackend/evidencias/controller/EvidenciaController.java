package com.utp.safezonebackend.evidencias.controller;

import com.utp.safezonebackend.evidencias.dto.request.ActualizarEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.request.VincularEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.evidencias.service.EvidenciaService;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Evidencia", description = "Gestion de evidencias de casos")
@RestController
@RequestMapping("/api/evidencias")
public class EvidenciaController {

    private final EvidenciaService service;

    public EvidenciaController(EvidenciaService service) {
        this.service = service;
    }

    @Operation(summary = "Listar evidencias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<EvidenciaResponse>> listar(
            @RequestParam(required = false) String casoId,
            @RequestParam(required = false) String denunciaId
    ) {
        return ResponseEntity.ok(service.findAll(casoId, denunciaId));
    }

    @Operation(summary = "Obtener evidencia por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/inactivar")
    public ResponseEntity<EvidenciaResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Crear evidencia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EvidenciaResponse> crear(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String casoId,
            @RequestParam(required = false) String denunciaId,
            Authentication authentication
    ) {
        String correo = authentication.getName();
        return ResponseEntity.ok(service.create(file, casoId, denunciaId, correo));
    }

    @Operation(summary = "Actualizar evidencia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}/inactivar")
    public ResponseEntity actualizar(@PathVariable String id, @RequestBody ActualizarEvidenciaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Eliminar evidencia")
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
    @PatchMapping("/{id}/vincular")
    public ResponseEntity<EvidenciaResponse> vincular(
            @PathVariable String id,
            @RequestBody VincularEvidenciaRequest request,
            Authentication authentication
    ) {
        String correo = authentication.getName();
        return ResponseEntity.ok(service.vincular(id, request, correo));
    }
}
