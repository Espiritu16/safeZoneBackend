package com.utp.safezonebackend.seguimientos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.seguimientos.dto.request.CrearSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.mapper.SeguimientoCasoMapper;
import com.utp.safezonebackend.seguimientos.repository.SeguimientoCasoRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeguimientoCasoServiceTest {

    @Mock
    private SeguimientoCasoRepository repository;

    @Mock
    private SeguimientoCasoMapper mapper;

    @Mock
    private CasoRepository casoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SeguimientoCasoService service;

    @Test
    void createRechazaAutorVictima() {
        Caso caso = new Caso();
        caso.setId("caso-1");
        caso.setEstado(EstadoCaso.EN_ATENCION);
        caso.setPrioridad(PrioridadCaso.ALTA);
        caso.setActivo(true);
        when(casoRepository.findByIdAndActivoTrue("caso-1")).thenReturn(Optional.of(caso));
        when(usuarioRepository.findById("victima-1")).thenReturn(Optional.of(usuario("victima-1", RolUsuario.VICTIMA)));

        assertThatThrownBy(() -> service.create(new CrearSeguimientoCasoRequest(
                "caso-1",
                "victima-1",
                "OBSERVACION",
                "Contenido del seguimiento",
                "Proxima llamada",
                OffsetDateTime.now().plusDays(2)
        ))).isInstanceOf(ExcepcionNegocio.class)
                .hasMessageContaining("Solo profesionales autorizados");
    }

    private Usuario usuario(String id, RolUsuario rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(rol);
        usuario.setActivo(true);
        return usuario;
    }
}
