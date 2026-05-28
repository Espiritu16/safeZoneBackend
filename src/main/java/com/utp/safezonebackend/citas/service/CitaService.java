package com.utp.safezonebackend.citas.service;

import com.utp.safezonebackend.citas.dto.request.CrearCitaRequest;
import com.utp.safezonebackend.citas.dto.request.ActualizarCitaRequest;
import com.utp.safezonebackend.citas.dto.response.CitaResponse;
import com.utp.safezonebackend.citas.mapper.CitaMapper;
import com.utp.safezonebackend.citas.repository.CitaRepository;
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

    public CitaResponse create(CrearCitaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public CitaResponse update(String id, ActualizarCitaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }
}
