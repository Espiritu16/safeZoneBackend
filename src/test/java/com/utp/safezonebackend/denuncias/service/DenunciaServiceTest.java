package com.utp.safezonebackend.denuncias.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.denuncias.dto.request.CrearDenunciaRequest;
import com.utp.safezonebackend.denuncias.dto.response.DenunciaResponse;
import com.utp.safezonebackend.denuncias.entity.Denuncia;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import com.utp.safezonebackend.denuncias.mapper.DenunciaMapper;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import com.utp.safezonebackend.evidencias.service.EvidenciaService;
import com.utp.safezonebackend.notificaciones.entity.Notificacion;
import com.utp.safezonebackend.notificaciones.repository.NotificacionRepository;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DenunciaServiceTest {

    @Mock
    private DenunciaRepository repository;

    @Mock
    private DenunciaMapper mapper;

    @Mock
    private CasoRepository casoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private EvidenciaService evidenciaService;

    @InjectMocks
    private DenunciaService service;

    @Test
    void createConRiesgoCriticoCreaCasoYNotificacion() {
        Usuario victima = usuario("victima-1", RolUsuario.VICTIMA);
        when(usuarioRepository.findById("victima-1")).thenReturn(Optional.of(victima));
        when(casoRepository.save(any(Caso.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(repository.save(any(Denuncia.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(notificacionRepository.save(any(Notificacion.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(Denuncia.class))).thenAnswer(invocation -> {
            Denuncia denuncia = invocation.getArgument(0);
            return new DenunciaResponse(denuncia.getId(), denuncia.getCasoId(), denuncia.getVictimaId(),
                    denuncia.getDescripcion(), denuncia.getTipoViolencia(), denuncia.getFechaIncidente(),
                    denuncia.getDistrito(), denuncia.getDireccionReferencia(), denuncia.getNivelRiesgo(),
                    denuncia.isAnonima(), denuncia.getAdjuntos(), denuncia.isActivo(),
                    denuncia.getFechaCreacion(), denuncia.getFechaActualizacion(), denuncia.getEdad());
        });

        DenunciaResponse response = service.create(new CrearDenunciaRequest(
                null,
                "victima-1",
                "Hecho reportado",
                "PSICOLOGICA",
                OffsetDateTime.now().minusDays(1),
                "Lima",
                "Referencia segura",
                NivelRiesgo.CRITICO,
                true,
                null,
                null
        ));

        assertThat(response.casoId()).isNotBlank();
        ArgumentCaptor<Caso> casoCaptor = ArgumentCaptor.forClass(Caso.class);
        verify(casoRepository).save(casoCaptor.capture());
        assertThat(casoCaptor.getValue().getEstado()).isEqualTo(EstadoCaso.REGISTRADO);
        assertThat(casoCaptor.getValue().getPrioridad()).isEqualTo(PrioridadCaso.CRITICA);

        ArgumentCaptor<Notificacion> notificacionCaptor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notificacionRepository).save(notificacionCaptor.capture());
        assertThat(notificacionCaptor.getValue().getUsuarioId()).isEqualTo("victima-1");
    }

    private Usuario usuario(String id, RolUsuario rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(rol);
        usuario.setActivo(true);
        return usuario;
    }
}
