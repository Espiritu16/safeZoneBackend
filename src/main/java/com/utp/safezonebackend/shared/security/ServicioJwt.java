package com.utp.safezonebackend.shared.security;

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
    private final long expiracionMinutos;

    public ServicioJwt(
            @Value("${app.jwt.secreto}") String secretoBase64,
            @Value("${app.jwt.expiracion-minutos:30}") long expiracionMinutos
    ) {
        byte[] bytes;
        try {
            bytes = Decoders.BASE64.decode(secretoBase64);
        } catch (Exception ex) {
            bytes = secretoBase64.getBytes(StandardCharsets.UTF_8);
        }
        this.llaveFirma = Keys.hmacShaKeyFor(bytes);
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarTokenAcceso(String usuarioId, String correo, String rol) {
        Instant ahora = Instant.now();
        Instant expira = ahora.plusSeconds(expiracionMinutos * 60);

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
