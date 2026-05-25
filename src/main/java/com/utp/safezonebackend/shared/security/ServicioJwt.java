package com.utp.safezonebackend.shared.security;

import com.utp.safezonebackend.configuracion.service.ConfiguracionSeguridadService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServicioJwt {

    private final SecretKey llaveFirma;
    private final ConfiguracionSeguridadService configuracionSeguridadService;

    public ServicioJwt(
            @Value("${app.jwt.secreto}") String secretoBase64,
            ConfiguracionSeguridadService configuracionSeguridadService
    ) {
        byte[] bytes;
        try {
            bytes = Decoders.BASE64.decode(secretoBase64);
        } catch (Exception ex) {
            bytes = secretoBase64.getBytes(StandardCharsets.UTF_8);
        }
        this.llaveFirma = Keys.hmacShaKeyFor(bytes);
        this.configuracionSeguridadService = configuracionSeguridadService;
    }

    public String generarTokenAcceso(String usuarioId, String correo, String rol) {
        Instant ahora = Instant.now();
        Instant expira = ahora.plusSeconds(configuracionSeguridadService.obtenerJwtExpiracionMinutos() * 60);

        return Jwts.builder()
                .subject(usuarioId)
                .claims(Map.of(
                        "correo", correo,
                        "rol", rol
                ))
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(expira))
                .signWith(llaveFirma)
                .compact();
    }

    public boolean esTokenValido(String token) {
        try {
            obtenerClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String obtenerUsuarioId(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        Object rol = obtenerClaims(token).get("rol");
        return rol == null ? null : rol.toString();
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(llaveFirma)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
