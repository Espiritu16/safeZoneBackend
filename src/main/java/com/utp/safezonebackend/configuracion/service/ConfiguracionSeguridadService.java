package com.utp.safezonebackend.configuracion.service;

import com.utp.safezonebackend.configuracion.entity.ConfiguracionSistema;
import com.utp.safezonebackend.configuracion.repository.ConfiguracionSistemaRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionSeguridadService {

    public static final String CLAVE_JWT_EXPIRACION_MINUTOS = "SEGURIDAD_JWT_EXPIRACION_MINUTOS";
    public static final String CLAVE_REFRESH_EXPIRACION_DIAS = "SEGURIDAD_REFRESH_EXPIRACION_DIAS";
    public static final String CLAVE_PASSWORD_MIN_LONGITUD = "SEGURIDAD_PASSWORD_MIN_LONGITUD";
    public static final String CLAVE_RECUPERACION_MAX_INTENTOS = "SEGURIDAD_RECUPERACION_MAX_INTENTOS";
    public static final String CLAVE_RECUPERACION_EXPIRACION_MINUTOS = "SEGURIDAD_RECUPERACION_EXPIRACION_MINUTOS";
    public static final String CLAVE_RIESGO_BAJO_MAXIMO = "SEGURIDAD_RIESGO_BAJO_MAXIMO";
    public static final String CLAVE_RIESGO_MEDIO_MAXIMO = "SEGURIDAD_RIESGO_MEDIO_MAXIMO";
    public static final String CLAVE_RIESGO_ALTO_MAXIMO = "SEGURIDAD_RIESGO_ALTO_MAXIMO";

    private final ConfiguracionSistemaRepository repository;
    private final long jwtExpiracionMinutosDefault;
    private final long refreshExpiracionDiasDefault;

    public ConfiguracionSeguridadService(
            ConfiguracionSistemaRepository repository,
            @Value("${app.jwt.expiracion-minutos:30}") long jwtExpiracionMinutosDefault,
            @Value("${app.auth.refresh-expiracion-dias:7}") long refreshExpiracionDiasDefault
    ) {
        this.repository = repository;
        this.jwtExpiracionMinutosDefault = jwtExpiracionMinutosDefault;
        this.refreshExpiracionDiasDefault = refreshExpiracionDiasDefault;
    }

    public long obtenerJwtExpiracionMinutos() {
        return obtenerLong(CLAVE_JWT_EXPIRACION_MINUTOS, jwtExpiracionMinutosDefault);
    }

    public long obtenerRefreshExpiracionDias() {
        return obtenerLong(CLAVE_REFRESH_EXPIRACION_DIAS, refreshExpiracionDiasDefault);
    }

    public int obtenerPasswordMinLongitud() {
        return obtenerEntero(CLAVE_PASSWORD_MIN_LONGITUD, 8);
    }

    public int obtenerRecuperacionMaxIntentos() {
        return obtenerEntero(CLAVE_RECUPERACION_MAX_INTENTOS, 5);
    }

    public int obtenerRecuperacionExpiracionMinutos() {
        return obtenerEntero(CLAVE_RECUPERACION_EXPIRACION_MINUTOS, 15);
    }

    public Map<String, Integer> obtenerCriteriosClasificacionRiesgo() {
        return Map.of(
                "bajoMaximo", obtenerEntero(CLAVE_RIESGO_BAJO_MAXIMO, 3),
                "medioMaximo", obtenerEntero(CLAVE_RIESGO_MEDIO_MAXIMO, 6),
                "altoMaximo", obtenerEntero(CLAVE_RIESGO_ALTO_MAXIMO, 9)
        );
    }

    public void validarContrasenaSegura(String contrasena) {
        int minimo = obtenerPasswordMinLongitud();
        if (contrasena == null || contrasena.length() < minimo) {
            throw new ExcepcionNegocio("La contrasena debe tener al menos " + minimo + " caracteres");
        }
        if (!contrasena.matches(".*[A-Z].*") || !contrasena.matches(".*\\d.*")) {
            throw new ExcepcionNegocio("La contrasena debe incluir al menos una mayuscula y un numero");
        }
    }

    private int obtenerEntero(String clave, int defecto) {
        long valor = obtenerLong(clave, defecto);
        if (valor > Integer.MAX_VALUE) {
            return defecto;
        }
        return (int) valor;
    }

    private long obtenerLong(String clave, long defecto) {
        return repository.findByClaveIgnoreCaseAndActivoTrue(clave)
                .map(ConfiguracionSistema::getValor)
                .map(valor -> parsearLong(valor, defecto))
                .orElse(defecto);
    }

    private long parsearLong(String valor, long defecto) {
        try {
            return Long.parseLong(valor.trim());
        } catch (RuntimeException ex) {
            return defecto;
        }
    }
}
