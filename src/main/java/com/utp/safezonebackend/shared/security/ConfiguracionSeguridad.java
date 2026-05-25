package com.utp.safezonebackend.shared.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracionSeguridad {

    private final FiltroAutenticacionJwt filtroAutenticacionJwt;

    public ConfiguracionSeguridad(FiltroAutenticacionJwt filtroAutenticacionJwt) {
        this.filtroAutenticacionJwt = filtroAutenticacionJwt;
    }

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(excepciones -> excepciones
                        .authenticationEntryPoint((request, response, authException) ->
                                escribirErrorSeguridad(response, HttpServletResponse.SC_UNAUTHORIZED, "No autenticado o token invalido"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                escribirErrorSeguridad(response, HttpServletResponse.SC_FORBIDDEN, "No tiene permisos para acceder a este recurso"))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/registrar",
                                "/api/auth/iniciar-sesion",
                                "/api/auth/renovar-token",
                                "/api/auth/cerrar-sesion",
                                "/api/auth/recuperar-contrasena",
                                "/api/auth/verificar-codigo",
                                "/api/auth/restablecer-contrasena",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/usuarios/**",
                                "/api/configuracion/**",
                                "/api/auditoria/**",
                                "/api/reportes/**"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(filtroAutenticacionJwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private void escribirErrorSeguridad(HttpServletResponse response, int status, String mensaje) throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"message\":\"" + mensaje + "\"}");
    }
}
