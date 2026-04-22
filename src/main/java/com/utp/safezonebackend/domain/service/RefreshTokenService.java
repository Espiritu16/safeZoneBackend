package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.CreateRefreshTokenRequest;
import com.utp.safezonebackend.domain.dto.request.UpdateRefreshTokenRequest;
import com.utp.safezonebackend.domain.dto.response.RefreshTokenResponse;
import com.utp.safezonebackend.domain.mapper.RefreshTokenMapper;
import com.utp.safezonebackend.domain.repository.RefreshTokenRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final RefreshTokenMapper mapper;

    public RefreshTokenService(RefreshTokenRepository repository, RefreshTokenMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RefreshTokenResponse> findAll() {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public RefreshTokenResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public RefreshTokenResponse create(CreateRefreshTokenRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public RefreshTokenResponse update(String id, UpdateRefreshTokenRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void delete(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
