package com.utp.safezonebackend.casos.service;

import com.utp.safezonebackend.casos.dto.request.CrearCasoRequest;
import com.utp.safezonebackend.casos.dto.request.ActualizarCasoRequest;
import com.utp.safezonebackend.casos.dto.response.CasoResponse;
import com.utp.safezonebackend.casos.mapper.CasoMapper;
import com.utp.safezonebackend.casos.repository.CasoRepository;
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

    public CasoResponse create(CrearCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public CasoResponse update(String id, ActualizarCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }
}
