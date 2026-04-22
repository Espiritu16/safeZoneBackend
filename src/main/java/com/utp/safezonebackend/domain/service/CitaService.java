package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateCitaRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateCitaRequest;
import com.utp.safezonebackend.domain.dto.response.CitaResponse;
import com.utp.safezonebackend.domain.mapper.CitaMapper;
import com.utp.safezonebackend.domain.repository.CitaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CitaService {

    private final CitaRepository repository;
    private final CitaMapper mapper;

    public CitaService(CitaRepository repository, CitaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CitaResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public CitaResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public CitaResponse create(CreateCitaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public CitaResponse update(String id, UpdateCitaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
