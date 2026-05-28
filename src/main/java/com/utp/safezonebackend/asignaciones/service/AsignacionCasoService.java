package com.utp.safezonebackend.asignaciones.service;

import com.utp.safezonebackend.asignaciones.dto.request.CrearAsignacionCasoRequest;
import com.utp.safezonebackend.asignaciones.dto.request.ActualizarAsignacionCasoRequest;
import com.utp.safezonebackend.asignaciones.dto.response.AsignacionCasoResponse;
import com.utp.safezonebackend.asignaciones.mapper.AsignacionCasoMapper;
import com.utp.safezonebackend.asignaciones.repository.AsignacionCasoRepository;
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

    public AsignacionCasoResponse create(CrearAsignacionCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public AsignacionCasoResponse update(String id, ActualizarAsignacionCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }
}
