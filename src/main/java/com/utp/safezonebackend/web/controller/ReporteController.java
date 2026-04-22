package com.utp.safezonebackend.web.controller;

import com.utp.safezonebackend.domain.dto.request.ReporteMensualRequest;
import com.utp.safezonebackend.domain.dto.response.ReporteMensualResponse;
import com.utp.safezonebackend.domain.service.ReporteService;
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
    public ResponseEntity<ReporteMensualResponse> generarMensual(@RequestBody ReporteMensualRequest request) {
        return ResponseEntity.ok(reporteService.generarMensual(request));
    }
}
