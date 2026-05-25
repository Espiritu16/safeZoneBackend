package com.utp.safezonebackend.usuarios.service;

import com.utp.safezonebackend.usuarios.dto.request.CreateUsuarioRequest;
import com.utp.safezonebackend.usuarios.dto.request.UpdateUsuarioRequest;
import com.utp.safezonebackend.usuarios.dto.response.UsuarioResponse;
import com.utp.safezonebackend.usuarios.mapper.UsuarioMapper;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UsuarioResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public UsuarioResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public UsuarioResponse create(CreateUsuarioRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public UsuarioResponse update(String id, UpdateUsuarioRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
