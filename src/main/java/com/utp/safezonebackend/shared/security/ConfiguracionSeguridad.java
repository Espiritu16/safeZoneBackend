package com.utp.safezonebackend.shared.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class ConfiguracionSeguridad {

    private final FiltroAutenticacionJwt filtroAutenticacionJwt;

    public ConfiguracionSeguridad(FiltroAutenticacionJwt filtroAutenticacionJwt) {
        this.filtroAutenticacionJwt = filtroAutenticacionJwt;
    }

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                                "/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/predenuncias").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/evidencias").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers(HttpMethod.POST, "/api/usuarios")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers(
                                "/api/auth/refresh-tokens/**",
                                "/api/usuarios/**",
                                "/api/configuracion/**",
                                "/api/auditoria/**",
                                "/api/reportes/**"
                        ).hasRole("ADMIN")
                        .requestMatchers("/api/panel-principal/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "PSICOLOGO", "DEFENSOR", "VICTIMA")
                        .requestMatchers(HttpMethod.GET, "/api/predenuncias/mis-registros")
                        .hasRole("VICTIMA")
                        .requestMatchers(HttpMethod.GET, "/api/victimas/me/historial")
                        .hasRole("VICTIMA")
                        .requestMatchers("/api/predenuncias/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .requestMatchers("/api/casos/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "PSICOLOGO", "DEFENSOR")
                        // Citas — profesionales y recepcionista
                        .requestMatchers("/api/citas/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "PSICOLOGO", "DEFENSOR")
                        // Víctimas y alias — personal autorizado
                        .requestMatchers("/api/victimas/**", "/api/victimasalias/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "PSICOLOGO", "DEFENSOR")
                        // Denuncias
                        .requestMatchers("/api/denuncias/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "DEFENSOR")
                        // Evidencias
                        .requestMatchers("/api/evidencias/**")
                        .hasAnyRole("ADMIN", "PSICOLOGO", "DEFENSOR")
                        // Seguimientos
                        .requestMatchers("/api/seguimientos/**")
                        .hasAnyRole("ADMIN", "PSICOLOGO", "DEFENSOR")
                        // Notificaciones — todos los autenticados
                        .requestMatchers("/api/notificaciones/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA", "PSICOLOGO", "DEFENSOR", "VICTIMA")
                        // Asignaciones
                        .requestMatchers("/api/asignaciones/**")
                        .hasAnyRole("ADMIN", "RECEPCIONISTA")
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(filtroAutenticacionJwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost",
                "http://localhost:*",
                "https://localhost",
                "https://localhost:*",
                "http://127.0.0.1",
                "http://127.0.0.1:*",
                "http://10.0.2.2",
                "http://10.0.2.2:*",
                "https://safezone.proyectoutp.com",
                "capacitor://localhost",
                "ionic://localhost"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    private void escribirErrorSeguridad(HttpServletResponse response, int status, String mensaje) throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"success\":false,\"message\":\"" + mensaje + "\"}");
    }
}
