package com.utp.safezonebackend.auth.controller;

import com.utp.safezonebackend.auth.dto.request.SolicitudLogin;
import com.utp.safezonebackend.auth.dto.request.SolicitudCerrarSesion;
import com.utp.safezonebackend.auth.dto.request.SolicitudRecuperarContrasena;
import com.utp.safezonebackend.auth.dto.request.SolicitudRegistro;
import com.utp.safezonebackend.auth.dto.request.SolicitudRenovarToken;
import com.utp.safezonebackend.auth.dto.request.SolicitudRestablecerContrasena;
import com.utp.safezonebackend.auth.dto.request.SolicitudVerificarCodigo;
import com.utp.safezonebackend.auth.dto.response.RespuestaBasica;
import com.utp.safezonebackend.auth.dto.response.RespuestaLogin;
import com.utp.safezonebackend.auth.dto.response.RespuestaRenovarToken;
import com.utp.safezonebackend.auth.service.AuthService;
import com.utp.safezonebackend.auth.service.RecuperacionContrasenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Endpoints de autenticacion y recuperacion de contrasena")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RecuperacionContrasenaService recuperacionContrasenaService;

    public AuthController(AuthService authService, RecuperacionContrasenaService recuperacionContrasenaService) {
        this.authService = authService;
        this.recuperacionContrasenaService = recuperacionContrasenaService;
    }

    @Operation(summary = "Registrar cuenta de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/registrar")
    public ResponseEntity<RespuestaBasica> registrar(@Valid @RequestBody SolicitudRegistro solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarUsuario(solicitud));
    }

    @Operation(summary = "Iniciar sesion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesion correcto"),
            @ApiResponse(responseCode = "400", description = "Credenciales invalidas"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/iniciar-sesion")
    public ResponseEntity<RespuestaLogin> iniciarSesion(@Valid @RequestBody SolicitudLogin solicitud) {
        return ResponseEntity.ok(authService.iniciarSesion(solicitud));
    }

    @Operation(summary = "Renovar token de acceso con refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renovado"),
            @ApiResponse(responseCode = "400", description = "Refresh token invalido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/renovar-token")
    public ResponseEntity<RespuestaRenovarToken> renovarToken(@Valid @RequestBody SolicitudRenovarToken solicitud) {
        return ResponseEntity.ok(authService.renovarToken(solicitud));
    }

    @Operation(summary = "Cerrar sesion revocando refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesion cerrada"),
            @ApiResponse(responseCode = "400", description = "Refresh token invalido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/cerrar-sesion")
    public ResponseEntity<RespuestaBasica> cerrarSesion(@Valid @RequestBody SolicitudCerrarSesion solicitud) {
        return ResponseEntity.ok(authService.cerrarSesion(solicitud));
    }

    @Operation(summary = "Solicitar codigo de recuperacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Codigo enviado"),
            @ApiResponse(responseCode = "404", description = "Correo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<RespuestaBasica> solicitarCodigo(@Valid @RequestBody SolicitudRecuperarContrasena solicitud) {
        return ResponseEntity.ok(recuperacionContrasenaService.solicitarCodigoRecuperacion(solicitud));
    }

    @Operation(summary = "Verificar codigo de recuperacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Codigo valido"),
            @ApiResponse(responseCode = "400", description = "Codigo invalido"),
            @ApiResponse(responseCode = "404", description = "Correo o codigo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/verificar-codigo")
    public ResponseEntity<RespuestaBasica> verificarCodigo(@Valid @RequestBody SolicitudVerificarCodigo solicitud) {
        return ResponseEntity.ok(recuperacionContrasenaService.verificarCodigoRecuperacion(solicitud));
    }

    @Operation(summary = "Restablecer contrasena")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrasena restablecida"),
            @ApiResponse(responseCode = "400", description = "Codigo invalido o solicitud invalida"),
            @ApiResponse(responseCode = "404", description = "Correo o codigo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<RespuestaBasica> restablecerContrasena(
            @Valid @RequestBody SolicitudRestablecerContrasena solicitud
    ) {
        return ResponseEntity.ok(recuperacionContrasenaService.restablecerContrasena(solicitud));
    }
}
