package com.utp.safezonebackend.reportes.controller;

import com.utp.safezonebackend.reportes.dto.request.ReporteMensualRequest;
import com.utp.safezonebackend.reportes.dto.response.ReporteMensualResponse;
import com.utp.safezonebackend.reportes.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Reporte", description = "Generacion de reportes del sistema")
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Generar reporte mensual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
        @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/mensual")
    public ResponseEntity<ReporteMensualResponse> generarMensual(@Valid @RequestBody ReporteMensualRequest request) {
        return ResponseEntity.ok(reporteService.generarMensual(request));
    }
}
