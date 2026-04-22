package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateSeguimientoCasoRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateSeguimientoCasoRequest;
import com.utp.safezonebackend.domain.dto.response.SeguimientoCasoResponse;
import com.utp.safezonebackend.domain.mapper.SeguimientoCasoMapper;
import com.utp.safezonebackend.domain.repository.SeguimientoCasoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SeguimientoCasoService {

    private final SeguimientoCasoRepository repository;
    private final SeguimientoCasoMapper mapper;

    public SeguimientoCasoService(SeguimientoCasoRepository repository, SeguimientoCasoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<SeguimientoCasoResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public SeguimientoCasoResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public SeguimientoCasoResponse create(CreateSeguimientoCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public SeguimientoCasoResponse update(String id, UpdateSeguimientoCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
