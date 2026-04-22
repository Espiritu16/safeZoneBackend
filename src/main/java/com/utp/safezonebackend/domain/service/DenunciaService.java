package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateDenunciaRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateDenunciaRequest;
import com.utp.safezonebackend.domain.dto.response.DenunciaResponse;
import com.utp.safezonebackend.domain.mapper.DenunciaMapper;
import com.utp.safezonebackend.domain.repository.DenunciaRepository;
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

    public DenunciaResponse create(CreateDenunciaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public DenunciaResponse update(String id, UpdateDenunciaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
