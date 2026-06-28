package com.utp.safezonebackend.predenuncias.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.denuncias.dto.request.CrearDenunciaRequest;
import com.utp.safezonebackend.denuncias.dto.response.DenunciaResponse;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import com.utp.safezonebackend.denuncias.service.DenunciaService;
import com.utp.safezonebackend.predenuncias.dto.request.CrearPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.request.FormalizarPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.response.PreDenunciaResponse;
import com.utp.safezonebackend.predenuncias.entity.PreDenuncia;
import com.utp.safezonebackend.predenuncias.enums.EstadoPreDenuncia;
import com.utp.safezonebackend.predenuncias.repository.PreDenunciaRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PreDenunciaServiceTest {

    @Mock
    private PreDenunciaRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private VictimaAliasRepository victimaAliasRepository;

    @Mock
    private AuditoriaService auditoriaService;

    @Mock
    private DenunciaService denunciaService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PreDenunciaService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void registrarPredenunciaDesdeVictimaAutenticadaLaVinculaConSuUsuario() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("victima@gmail.com", "N/A"));
        Usuario victima = usuario("victima-1", RolUsuario.VICTIMA);
        when(usuarioRepository.buscarPorCorreo("victima@gmail.com")).thenReturn(Optional.of(victima));
        when(repository.save(any(PreDenuncia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PreDenunciaResponse response = service.registrar(new CrearPreDenunciaRequest(
                "Maria",
                "Victima",
                "999888777",
                "victima@gmail.com",
                "Descripcion detallada para registrar una predenuncia vinculada",
                "FISICA",
                OffsetDateTime.now().minusDays(1),
                "Comas",
                "Registrada desde el panel de victima",
                false
        ));

        assertThat(response.victimaId()).isEqualTo("victima-1");

        ArgumentCaptor<PreDenuncia> captor = ArgumentCaptor.forClass(PreDenuncia.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getVictimaId()).isEqualTo("victima-1");
        assertThat(captor.getValue().getCreadoPor()).isEqualTo("victima-1");
        assertThat(captor.getValue().isAnonima()).isFalse();
    }

    @Test
    void listarMisRegistrosDevuelvePredenunciasDeLaVictimaAutenticada() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("victima@gmail.com", "N/A"));
        Usuario victima = usuario("victima-1", RolUsuario.VICTIMA);
        PreDenuncia preDenuncia = preDenuncia(EstadoPreDenuncia.PENDIENTE);
        preDenuncia.setVictimaId("victima-1");

        when(usuarioRepository.buscarPorCorreo("victima@gmail.com")).thenReturn(Optional.of(victima));
        when(repository.findByVictimaIdAndActivoTrueOrderByFechaCreacionDesc("victima-1")).thenReturn(List.of(preDenuncia));

        List<PreDenunciaResponse> response = service.listarMisRegistros();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().victimaId()).isEqualTo("victima-1");
    }

    @Test
    void formalizarPredenunciaEnContactoCreaDenunciaYCasoDesdeDatosRegistrados() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin@safezone.local", "N/A"));
        PreDenuncia preDenuncia = preDenuncia(EstadoPreDenuncia.EN_CONTACTO);
        Usuario admin = usuario("admin-1", RolUsuario.ADMIN);
        DenunciaResponse denuncia = new DenunciaResponse(
                "denuncia-1",
                "caso-1",
                "victima-1",
                preDenuncia.getDescripcionHecho(),
                preDenuncia.getTipoViolencia(),
                preDenuncia.getFechaIncidente(),
                preDenuncia.getDistrito(),
                preDenuncia.getDireccionReferencia(),
                NivelRiesgo.ALTO,
                preDenuncia.isAnonima(),
                null,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                null
        );

        when(repository.findById("pre-1")).thenReturn(Optional.of(preDenuncia));
        when(usuarioRepository.buscarPorCorreo("admin@safezone.local")).thenReturn(Optional.of(admin));
        when(victimaAliasRepository.findTopByVictimaIdAndActivoTrueOrderByFechaAsignacionDesc("victima-1")).thenReturn(Optional.empty());
        when(usuarioRepository.findById("victima-1")).thenReturn(Optional.of(usuario("victima-1", RolUsuario.VICTIMA)));
        when(denunciaService.create(any(CrearDenunciaRequest.class))).thenReturn(denuncia);
        when(repository.save(any(PreDenuncia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PreDenunciaResponse response = service.formalizar("pre-1", new FormalizarPreDenunciaRequest(
                "victima-1",
                null,
                null,
                NivelRiesgo.ALTO,
                false,
                null
        ));

        assertThat(response.estado()).isEqualTo(EstadoPreDenuncia.FORMALIZADA);
        assertThat(response.denunciaId()).isEqualTo("denuncia-1");
        assertThat(response.casoId()).isEqualTo("caso-1");
        assertThat(response.victimaId()).isEqualTo("victima-1");

        ArgumentCaptor<CrearDenunciaRequest> captor = ArgumentCaptor.forClass(CrearDenunciaRequest.class);
        verify(denunciaService).create(captor.capture());
        assertThat(captor.getValue().descripcion()).isEqualTo(preDenuncia.getDescripcionHecho());
        assertThat(captor.getValue().tipoViolencia()).isEqualTo(preDenuncia.getTipoViolencia());
        assertThat(captor.getValue().nivelRiesgo()).isEqualTo(NivelRiesgo.ALTO);
    }

    @Test
    void formalizarRechazaPredenunciaPendienteAntesDeContactar() {
        when(repository.findById("pre-1")).thenReturn(Optional.of(preDenuncia(EstadoPreDenuncia.PENDIENTE)));

        assertThatThrownBy(() -> service.formalizar("pre-1", new FormalizarPreDenunciaRequest(
                "victima-1",
                null,
                null,
                NivelRiesgo.ALTO,
                false,
                null
        ))).isInstanceOf(ExcepcionNegocio.class)
                .hasMessageContaining("Debe marcar la predenuncia en contacto");
    }

    @Test
    void formalizarAnonimaCreaVictimaProtegidaSinExigirDniReal() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin@safezone.local", "N/A"));
        PreDenuncia preDenuncia = preDenuncia(EstadoPreDenuncia.EN_CONTACTO);
        Usuario admin = usuario("admin-1", RolUsuario.ADMIN);
        Usuario victimaProtegida = usuario("victima-alias-1", RolUsuario.VICTIMA);
        victimaProtegida.setDni("ALIAS12345678");
        DenunciaResponse denuncia = new DenunciaResponse(
                "denuncia-alias-1",
                "caso-alias-1",
                "victima-alias-1",
                preDenuncia.getDescripcionHecho(),
                preDenuncia.getTipoViolencia(),
                preDenuncia.getFechaIncidente(),
                preDenuncia.getDistrito(),
                preDenuncia.getDireccionReferencia(),
                NivelRiesgo.CRITICO,
                true,
                null,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                null
        );

        when(repository.findById("pre-1")).thenReturn(Optional.of(preDenuncia));
        when(usuarioRepository.buscarPorCorreo("admin@safezone.local")).thenReturn(Optional.of(admin));
        when(usuarioRepository.existsByDni(org.mockito.ArgumentMatchers.startsWith("ALIAS"))).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(victimaProtegida);
        when(usuarioRepository.findById("victima-alias-1")).thenReturn(Optional.of(victimaProtegida));
        when(victimaAliasRepository.save(any(VictimaAlias.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(denunciaService.create(any(CrearDenunciaRequest.class))).thenReturn(denuncia);
        when(repository.save(any(PreDenuncia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PreDenunciaResponse response = service.formalizar("pre-1", new FormalizarPreDenunciaRequest(
                null,
                null,
                null,
                NivelRiesgo.CRITICO,
                true,
                null
        ));

        assertThat(response.estado()).isEqualTo(EstadoPreDenuncia.FORMALIZADA);
        assertThat(response.victimaId()).isEqualTo("victima-alias-1");
        assertThat(response.denunciaId()).isEqualTo("denuncia-alias-1");
        assertThat(response.casoId()).isEqualTo("caso-alias-1");

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        assertThat(usuarioCaptor.getValue().getDni()).startsWith("ALIAS");
        assertThat(usuarioCaptor.getValue().getNombres()).isEqualTo("Victima protegida");
        assertThat(usuarioCaptor.getValue().getRol()).isEqualTo(RolUsuario.VICTIMA);
    }

    private PreDenuncia preDenuncia(EstadoPreDenuncia estado) {
        PreDenuncia preDenuncia = new PreDenuncia();
        preDenuncia.setId("pre-1");
        preDenuncia.setActivo(true);
        preDenuncia.setEstado(estado);
        preDenuncia.setNombresContacto("Maria");
        preDenuncia.setTelefonoContacto("999999999");
        preDenuncia.setDescripcionHecho("Descripcion detallada de prueba");
        preDenuncia.setTipoViolencia("FISICA");
        preDenuncia.setFechaIncidente(OffsetDateTime.now().minusDays(1));
        preDenuncia.setDistrito("Lima");
        preDenuncia.setDireccionReferencia("Horario: tarde");
        preDenuncia.setAnonima(false);
        return preDenuncia;
    }

    private Usuario usuario(String id, RolUsuario rol) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setRol(rol);
        usuario.setActivo(true);
        return usuario;
    }
}
