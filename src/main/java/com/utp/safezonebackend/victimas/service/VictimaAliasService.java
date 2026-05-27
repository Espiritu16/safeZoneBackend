package com.utp.safezonebackend.victimas.service;


import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.victimas.dto.request.CreateVictimaAliasRequest;
import com.utp.safezonebackend.victimas.dto.request.InhabilitarVictimaAliasRequest;
import com.utp.safezonebackend.victimas.dto.request.UpdateVictimaAliasRequest;
import com.utp.safezonebackend.victimas.dto.response.VictimaAliasResponse;
import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import com.utp.safezonebackend.victimas.mapper.VictimaAliasMapper;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class VictimaAliasService {

    private final VictimaAliasRepository repository;
    private final VictimaAliasMapper mapper;

    public VictimaAliasService(VictimaAliasRepository repository, VictimaAliasMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    public VictimaAliasResponse findById(String id) {
        VictimaAlias alias = obtenerAlias(id);
        VictimaAliasResponse response = new VictimaAliasResponse();
        response.setAliasCodigo(alias.getAliasCodigo());
        response.setCreadoPor(alias.getCreadoPor());
        response.setFechaAsignacion(alias.getFechaAsignacion());
        response.setFechaFin(alias.getFechaFin());
        return response;
    }

    public VictimaAliasResponse create(CreateVictimaAliasRequest request) {
        String aliasGenerado = "VIC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        VictimaAlias alias = new VictimaAlias();
        alias.setAliasCodigo(aliasGenerado);
        alias.setVictimaId(request.getVictimaId());
        alias.setCreadoPor(request.getCreadoPor());
        alias.setFechaAsignacion(OffsetDateTime.now());
        alias.setFechaFin(request.getFechaFin());
        alias.setActivo(true);
        VictimaAlias guardado = repository.save(alias);
        VictimaAliasResponse response = new VictimaAliasResponse();
        response.setAliasCodigo(guardado.getAliasCodigo());
        response.setCreadoPor(guardado.getCreadoPor());
        response.setFechaAsignacion(guardado.getFechaAsignacion());
        response.setFechaFin(guardado.getFechaFin());
        return response;
    }

    public VictimaAliasResponse update(String id, UpdateVictimaAliasRequest request) {
        VictimaAlias alias = obtenerAlias(id);
        alias.setActualizadoPor(request.getActualizadoPor());
        alias.setFechaActualizacion(OffsetDateTime.now());
        VictimaAlias guardado = repository.save(alias);
        VictimaAliasResponse response = new VictimaAliasResponse();
        response.setAliasCodigo(guardado.getAliasCodigo());
        response.setCreadoPor(guardado.getCreadoPor());
        response.setFechaAsignacion(guardado.getFechaAsignacion());
        response.setFechaFin(guardado.getFechaFin());
        return response;
    }

    public void inactivar(String id, InhabilitarVictimaAliasRequest request) {
        VictimaAlias alias = obtenerAlias(id);
        alias.setActivo(false);
        alias.setInactivadoPor(request.getInactivadoPor());
        alias.setFechaInactivacion(OffsetDateTime.now());
        alias.setFechaFin(OffsetDateTime.now());
        repository.save(alias);
    }
    private VictimaAlias obtenerAlias(String id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Alias no encontrado"));
    }
}
