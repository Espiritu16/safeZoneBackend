package com.utp.safezonebackend.victimas.controller;

import com.utp.safezonebackend.victimas.dto.response.VictimaHistorialResponse;
import com.utp.safezonebackend.victimas.service.VictimaHistorialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/victimas")
public class VictimaController {

    private final VictimaHistorialService historialService;

    public VictimaController(VictimaHistorialService historialService) {
        this.historialService = historialService;
    }

    @GetMapping("/{victimaId}/historial")
    public ResponseEntity<VictimaHistorialResponse> obtenerHistorialPorVictima(@PathVariable String victimaId) {
        return ResponseEntity.ok(historialService.obtenerPorVictimaId(victimaId));
    }

    @GetMapping("/alias/{aliasCodigo}/historial")
    public ResponseEntity<VictimaHistorialResponse> obtenerHistorialPorAlias(@PathVariable String aliasCodigo) {
        return ResponseEntity.ok(historialService.obtenerPorAlias(aliasCodigo));
    }
}
