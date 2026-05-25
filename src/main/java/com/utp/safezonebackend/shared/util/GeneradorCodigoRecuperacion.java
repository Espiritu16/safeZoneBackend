package com.utp.safezonebackend.shared.util;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class GeneradorCodigoRecuperacion {

    private static final SecureRandom RANDOM = new SecureRandom();

    public String generarCodigo() {
        int numero = RANDOM.nextInt(1_000_000);
        return String.format("%06d", numero);
    }
}

