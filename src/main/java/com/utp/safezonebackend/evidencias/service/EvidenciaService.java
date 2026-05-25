package com.utp.safezonebackend.evidencias.service;

import com.utp.safezonebackend.evidencias.dto.request.CreateEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.request.UpdateEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.evidencias.mapper.EvidenciaMapper;
import com.utp.safezonebackend.evidencias.repository.EvidenciaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EvidenciaService {

    private final EvidenciaRepository repository;
    private final EvidenciaMapper mapper;

    public EvidenciaService(EvidenciaRepository repository, EvidenciaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<EvidenciaResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public EvidenciaResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public EvidenciaResponse create(CreateEvidenciaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public EvidenciaResponse update(String id, UpdateEvidenciaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }
}
