package com.utp.safezonebackend.predenuncias.controller;

import com.utp.safezonebackend.predenuncias.dto.request.CrearPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.request.DescartarPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.request.FormalizarPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.response.PreDenunciaResponse;
import com.utp.safezonebackend.predenuncias.enums.EstadoPreDenuncia;
import com.utp.safezonebackend.predenuncias.service.PreDenunciaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predenuncias")
public class PreDenunciaController {

    private final PreDenunciaService service;

    public PreDenunciaController(PreDenunciaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PreDenunciaResponse> registrar(@Valid @RequestBody CrearPreDenunciaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<PreDenunciaResponse>> listar(@RequestParam(required = false) EstadoPreDenuncia estado) {
        return ResponseEntity.ok(service.listar(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreDenunciaResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PatchMapping("/{id}/contactar")
    public ResponseEntity<PreDenunciaResponse> marcarEnContacto(@PathVariable String id) {
        return ResponseEntity.ok(service.marcarEnContacto(id));
    }

    @PatchMapping("/{id}/formalizar")
    public ResponseEntity<PreDenunciaResponse> formalizar(
            @PathVariable String id,
            @Valid @RequestBody FormalizarPreDenunciaRequest request
    ) {
        return ResponseEntity.ok(service.formalizar(id, request));
    }

    @PatchMapping("/{id}/descartar")
    public ResponseEntity<PreDenunciaResponse> descartar(
            @PathVariable String id,
            @Valid @RequestBody DescartarPreDenunciaRequest request
    ) {
        return ResponseEntity.ok(service.descartar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactivar(@PathVariable String id) {
        service.inactivar(id);
        return ResponseEntity.noContent().build();
    }
}
