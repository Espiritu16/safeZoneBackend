package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateVictimaAliasRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateVictimaAliasRequest;
import com.utp.safezonebackend.domain.dto.response.VictimaAliasResponse;
import com.utp.safezonebackend.domain.mapper.VictimaAliasMapper;
import com.utp.safezonebackend.domain.repository.VictimaAliasRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VictimaAliasService {

    private final VictimaAliasRepository repository;
    private final VictimaAliasMapper mapper;

    public VictimaAliasService(VictimaAliasRepository repository, VictimaAliasMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<VictimaAliasResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public VictimaAliasResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public VictimaAliasResponse create(CreateVictimaAliasRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public VictimaAliasResponse update(String id, UpdateVictimaAliasRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
