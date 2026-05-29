package com.utp.safezonebackend.seguimientos.service;

import com.utp.safezonebackend.seguimientos.dto.request.CrearSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.dto.request.ActualizarSeguimientoCasoRequest;
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

    public SeguimientoCasoResponse create(CrearSeguimientoCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public SeguimientoCasoResponse update(String id, ActualizarSeguimientoCasoRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }
}
