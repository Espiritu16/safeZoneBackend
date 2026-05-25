package com.utp.safezonebackend.auditoria.service;

import com.utp.safezonebackend.auditoria.dto.request.CreateAuditoriaRequest;
import com.utp.safezonebackend.auditoria.dto.request.RegistroAuditoriaInterna;
import com.utp.safezonebackend.auditoria.dto.request.UpdateAuditoriaRequest;
import com.utp.safezonebackend.auditoria.dto.response.AuditoriaResponse;
import com.utp.safezonebackend.auditoria.entity.Auditoria;
import com.utp.safezonebackend.auditoria.enums.ResultadoAuditoria;
import com.utp.safezonebackend.auditoria.mapper.AuditoriaMapper;
import com.utp.safezonebackend.auditoria.repository.AuditoriaRepository;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaService {

    private final AuditoriaRepository repository;
    private final AuditoriaMapper mapper;

    public AuditoriaService(AuditoriaRepository repository, AuditoriaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<AuditoriaResponse> findAll() {
        return repository.findAllByOrderByFechaDesc().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AuditoriaResponse findById(String id) {
        Auditoria auditoria = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro de auditoria no encontrado"));
        return mapper.toResponse(auditoria);
    }

    @Transactional
    public AuditoriaResponse create(CreateAuditoriaRequest request) {
        throw new UnsupportedOperationException("Creacion manual de auditoria no permitida");
    }

    @Transactional
    public AuditoriaResponse update(String id, UpdateAuditoriaRequest request) {
        throw new UnsupportedOperationException("Actualizacion manual de auditoria no permitida");
    }

    @Transactional
    public void delete(String id) {
        throw new UnsupportedOperationException("Eliminacion manual de auditoria no permitida");
    }

    @Transactional
    public void registrarAccion(RegistroAuditoriaInterna registro) {
        Auditoria auditoria = new Auditoria();
        auditoria.setId(UUID.randomUUID().toString());
        auditoria.setEntidadTipo(registro.entidadTipo());
        auditoria.setFecha(OffsetDateTime.now());
        auditoria.setActorId(registro.actorId());
        auditoria.setActorRol(registro.rolActor());
        auditoria.setAccion(registro.accion());
        auditoria.setEntidadId(registro.entidadId());
        auditoria.setResultado(registro.resultado() == null ? ResultadoAuditoria.OK : registro.resultado());
        auditoria.setDetalle(registro.detalle());
        auditoria.setAntes(registro.antes());
        auditoria.setDespues(registro.despues());
        auditoria.setIp(registro.ip());
        auditoria.setUserAgent(registro.agenteUsuario());
        auditoria.setRequestId(registro.codigoSolicitud());
        auditoria.setFechaCreacion(OffsetDateTime.now());
        repository.save(auditoria);
    }
}
