package com.utp.safezonebackend.auth.service;

import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorreoService.class);

    private final JavaMailSender mailSender;
    private final boolean modoSimulacion;
    private final String remitente;

    public CorreoService(
            JavaMailSender mailSender,
            @Value("${app.correo.modo-simulacion:true}") boolean modoSimulacion,
            @Value("${app.correo.remitente:no-reply@safezone.local}") String remitente
    ) {
        this.mailSender = mailSender;
        this.modoSimulacion = modoSimulacion;
        this.remitente = remitente;
    }

    public void enviarCodigoRecuperacion(String correoDestino, String codigo) {
        if (modoSimulacion) {
            LOGGER.info("Modo simulacion activo. Se genero codigo de recuperacion para {}", correoDestino);
            return;
        }

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(correoDestino);
            mensaje.setSubject("SafeZone - Codigo de recuperacion");
            mensaje.setText(
                    "Tu codigo de recuperacion es: " + codigo + "\n\n"
                            + "Este codigo expira en 15 minutos."
            );
            mailSender.send(mensaje);
        } catch (MailException ex) {
            throw new ExcepcionNegocio("No se pudo enviar el correo de recuperacion");
        }
    }
}

