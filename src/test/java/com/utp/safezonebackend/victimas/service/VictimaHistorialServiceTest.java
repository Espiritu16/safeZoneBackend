package com.utp.safezonebackend.victimas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.dto.response.VictimaHistorialResponse;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class VictimaHistorialServiceTest {

    @Mock
    private VictimaAliasRepository victimaAliasRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void obtenerHistorialAutenticadoUsaLaVictimaDeLaSesion() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("victima@gmail.com", "N/A"));
        Usuario victima = new Usuario();
        victima.setId("victima-1");
        victima.setCorreo("victima@gmail.com");
        victima.setRol(RolUsuario.VICTIMA);
        victima.setActivo(true);

        VictimaHistorialResponse esperado = new VictimaHistorialResponse(
                "victima-1",
                null,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        VictimaHistorialService service = Mockito.spy(new VictimaHistorialService(victimaAliasRepository, usuarioRepository));
        when(usuarioRepository.buscarPorCorreo("victima@gmail.com")).thenReturn(Optional.of(victima));
        doReturn(esperado).when(service).obtenerPorVictimaId("victima-1");

        VictimaHistorialResponse response = service.obtenerHistorialAutenticado();

        assertThat(response.victimaId()).isEqualTo("victima-1");
        verify(service).obtenerPorVictimaId("victima-1");
    }
}
