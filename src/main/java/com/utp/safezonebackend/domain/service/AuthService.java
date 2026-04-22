package com.utp.safezonebackend.domain.service;

import com.utp.safezonebackend.domain.dto.request.LoginRequest;
import com.utp.safezonebackend.domain.dto.request.RefreshTokenRequest;
import com.utp.safezonebackend.domain.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public AuthResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void logout(RefreshTokenRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
