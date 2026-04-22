package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateCasoRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateCasoRequest;
import com.utp.safezonebackend.domain.dto.response.CasoResponse;
import com.utp.safezonebackend.domain.mapper.CasoMapper;
import com.utp.safezonebackend.domain.repository.CasoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CasoService {

    private final CasoRepository repository;
    private final CasoMapper mapper;

    public CasoService(CasoRepository repository, CasoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CasoResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public CasoResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public CasoResponse create(CreateCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public CasoResponse update(String id, UpdateCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
