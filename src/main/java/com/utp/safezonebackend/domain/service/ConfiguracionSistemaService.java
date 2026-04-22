package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateConfiguracionSistemaRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateConfiguracionSistemaRequest;
import com.utp.safezonebackend.domain.dto.response.ConfiguracionSistemaResponse;
import com.utp.safezonebackend.domain.mapper.ConfiguracionSistemaMapper;
import com.utp.safezonebackend.domain.repository.ConfiguracionSistemaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionSistemaService {

    private final ConfiguracionSistemaRepository repository;
    private final ConfiguracionSistemaMapper mapper;

    public ConfiguracionSistemaService(ConfiguracionSistemaRepository repository, ConfiguracionSistemaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ConfiguracionSistemaResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public ConfiguracionSistemaResponse findById(Long id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public ConfiguracionSistemaResponse create(CreateConfiguracionSistemaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public ConfiguracionSistemaResponse update(Long id, UpdateConfiguracionSistemaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
