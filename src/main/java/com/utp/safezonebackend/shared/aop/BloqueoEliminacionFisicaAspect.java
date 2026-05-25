package com.utp.safezonebackend.shared.aop;

import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BloqueoEliminacionFisicaAspect {

    @Before("execution(* org.springframework.data.jpa.repository.JpaRepository+.delete*(..))")
    public void bloquearDeleteFisico() {
        throw new ExcepcionNegocio(
                "No se permite eliminacion fisica. Use inactivacion por estado/activo."
        );
    }
}
