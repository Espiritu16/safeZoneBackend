package com.utp.safezonebackend.denuncias.service;

import com.utp.safezonebackend.denuncias.dto.request.CrearDenunciaRequest;
import com.utp.safezonebackend.denuncias.dto.request.ActualizarDenunciaRequest;
import com.utp.safezonebackend.denuncias.dto.response.DenunciaResponse;
import com.utp.safezonebackend.denuncias.mapper.DenunciaMapper;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DenunciaService {

    private final DenunciaRepository repository;
    private final DenunciaMapper mapper;

    public DenunciaService(DenunciaRepository repository, DenunciaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<DenunciaResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public DenunciaResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public DenunciaResponse create(CrearDenunciaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public DenunciaResponse update(String id, ActualizarDenunciaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }
}
