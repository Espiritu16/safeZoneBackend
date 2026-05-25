package com.utp.safezonebackend.auth.service;

import com.utp.safezonebackend.auth.dto.request.CreateRefreshTokenRequest;
import com.utp.safezonebackend.auth.dto.request.UpdateRefreshTokenRequest;
import com.utp.safezonebackend.auth.dto.response.RefreshTokenResponse;
import com.utp.safezonebackend.auth.entity.RefreshToken;
import com.utp.safezonebackend.auth.mapper.RefreshTokenMapper;
import com.utp.safezonebackend.auth.repository.RefreshTokenRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final RefreshTokenMapper mapper;

    public RefreshTokenService(RefreshTokenRepository repository, RefreshTokenMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<RefreshTokenResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RefreshTokenResponse findById(String id) {
        return mapper.toResponse(obtenerToken(id));
    }

    public RefreshTokenResponse create(CreateRefreshTokenRequest request) {
        throw new ExcepcionNegocio("La creacion manual de refresh tokens no esta permitida");
    }

    public RefreshTokenResponse update(String id, UpdateRefreshTokenRequest request) {
        throw new ExcepcionNegocio("La actualizacion manual de refresh tokens no esta permitida");
    }

    @Transactional
    public void inactivar(String id) {
        RefreshToken token = obtenerToken(id);
        token.setActivo(false);
        token.setRevocado(true);
        token.setFechaRevocacion(OffsetDateTime.now());
        repository.save(token);
    }

    private RefreshToken obtenerToken(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Refresh token no encontrado"));
    }
}
