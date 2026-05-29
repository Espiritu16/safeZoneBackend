package com.utp.safezonebackend.panel.controller;

import com.utp.safezonebackend.panel.dto.response.PanelRolResponse;
import com.utp.safezonebackend.panel.service.PanelPrincipalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panel-principal")
public class PanelPrincipalController {

    private final PanelPrincipalService service;

    public PanelPrincipalController(PanelPrincipalService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<PanelRolResponse> obtenerPanelActual() {
        return ResponseEntity.ok(service.obtenerPanelActual());
    }
}
