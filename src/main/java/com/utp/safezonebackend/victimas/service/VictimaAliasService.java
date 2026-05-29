package com.utp.safezonebackend.victimas.service;


import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.dto.request.CrearVictimaAliasRequest;
import com.utp.safezonebackend.victimas.dto.request.InhabilitarVictimaAliasRequest;
import com.utp.safezonebackend.victimas.dto.request.ActualizarVictimaAliasRequest;
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
    private final UsuarioRepository usuarioRepository;
    public VictimaAliasService(VictimaAliasRepository repository, VictimaAliasMapper mapper,UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository=usuarioRepository;
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

    public VictimaAliasResponse create(CrearVictimaAliasRequest request) {
        Usuario victima = usuarioRepository.findById(request.getVictimaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Víctima no encontrada"));
        String aliasGenerado = "VIC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        OffsetDateTime ahora = OffsetDateTime.now();
        VictimaAlias alias = new VictimaAlias();
        alias.setId(UUID.randomUUID().toString());
        alias.setAliasCodigo(aliasGenerado);
        alias.setVictima(victima);
        alias.setCreadoPor(request.getCreadoPor());
        alias.setFechaAsignacion(ahora);
        alias.setFechaActualizacion(ahora);
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

    public VictimaAliasResponse update(String id, ActualizarVictimaAliasRequest request) {
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
