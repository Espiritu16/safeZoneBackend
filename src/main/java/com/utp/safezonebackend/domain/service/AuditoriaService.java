package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateAuditoriaRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateAuditoriaRequest;
import com.utp.safezonebackend.domain.dto.response.AuditoriaResponse;
import com.utp.safezonebackend.domain.mapper.AuditoriaMapper;
import com.utp.safezonebackend.domain.repository.AuditoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    private final AuditoriaRepository repository;
    private final AuditoriaMapper mapper;

    public AuditoriaService(AuditoriaRepository repository, AuditoriaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AuditoriaResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public AuditoriaResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public AuditoriaResponse create(CreateAuditoriaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public AuditoriaResponse update(String id, UpdateAuditoriaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
