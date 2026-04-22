package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateAsignacionCasoRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateAsignacionCasoRequest;
import com.utp.safezonebackend.domain.dto.response.AsignacionCasoResponse;
import com.utp.safezonebackend.domain.mapper.AsignacionCasoMapper;
import com.utp.safezonebackend.domain.repository.AsignacionCasoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AsignacionCasoService {

    private final AsignacionCasoRepository repository;
    private final AsignacionCasoMapper mapper;

    public AsignacionCasoService(AsignacionCasoRepository repository, AsignacionCasoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AsignacionCasoResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public AsignacionCasoResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public AsignacionCasoResponse create(CreateAsignacionCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public AsignacionCasoResponse update(String id, UpdateAsignacionCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
