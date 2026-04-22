package com.utp.safezonebackend.seguimientos.service;

import com.utp.safezonebackend.seguimientos.dto.request.CreateSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.dto.request.UpdateSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.dto.response.SeguimientoCasoResponse;
import com.utp.safezonebackend.seguimientos.mapper.SeguimientoCasoMapper;
import com.utp.safezonebackend.seguimientos.repository.SeguimientoCasoRepository;
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
