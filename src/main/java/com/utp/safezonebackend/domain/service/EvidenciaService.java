package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateEvidenciaRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateEvidenciaRequest;
import com.utp.safezonebackend.domain.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.domain.mapper.EvidenciaMapper;
import com.utp.safezonebackend.domain.repository.EvidenciaRepository;
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

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
