package com.utp.safezonebackend.shared.exception;

import com.utp.safezonebackend.auth.dto.response.RespuestaBasica;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(ExcepcionAutenticacion.class)
    public ResponseEntity<RespuestaBasica> manejarAutenticacion(ExcepcionAutenticacion ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RespuestaBasica(false, ex.getMessage()));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<RespuestaBasica> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespuestaBasica(false, ex.getMessage()));
    }

    @ExceptionHandler(ExcepcionNegocio.class)
    public ResponseEntity<RespuestaBasica> manejarNegocio(ExcepcionNegocio ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RespuestaBasica(false, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespuestaBasica> manejarValidacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatearErrorCampo)
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(new RespuestaBasica(false, mensaje));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RespuestaBasica> manejarConstraint(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(new RespuestaBasica(false, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaBasica> manejarGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RespuestaBasica(false, "Error interno del servidor"));
    }

    private String formatearErrorCampo(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
