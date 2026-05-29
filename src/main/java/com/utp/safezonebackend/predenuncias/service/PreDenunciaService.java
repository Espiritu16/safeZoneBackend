package com.utp.safezonebackend.predenuncias.service;

import com.utp.safezonebackend.auditoria.dto.request.RegistroAuditoriaInterna;
import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.predenuncias.dto.request.CrearPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.request.DescartarPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.request.FormalizarPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.response.PreDenunciaResponse;
import com.utp.safezonebackend.predenuncias.entity.PreDenuncia;
import com.utp.safezonebackend.predenuncias.enums.EstadoPreDenuncia;
import com.utp.safezonebackend.predenuncias.repository.PreDenunciaRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PreDenunciaService {

    private final PreDenunciaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final VictimaAliasRepository victimaAliasRepository;
    private final AuditoriaService auditoriaService;

    public PreDenunciaService(
            PreDenunciaRepository repository,
            UsuarioRepository usuarioRepository,
            VictimaAliasRepository victimaAliasRepository,
            AuditoriaService auditoriaService
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.victimaAliasRepository = victimaAliasRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public PreDenunciaResponse registrar(CrearPreDenunciaRequest request) {
        validarContactoSeguro(request.telefonoContacto(), request.correoContacto());
        OffsetDateTime ahora = OffsetDateTime.now();
        PreDenuncia preDenuncia = new PreDenuncia();
        preDenuncia.setId(UUID.randomUUID().toString());
        preDenuncia.setNombresContacto(limpiar(request.nombresContacto()));
        preDenuncia.setApellidosContacto(limpiar(request.apellidosContacto()));
        preDenuncia.setTelefonoContacto(limpiar(request.telefonoContacto()));
        preDenuncia.setCorreoContacto(limpiar(request.correoContacto()));
        preDenuncia.setDescripcionHecho(limpiar(request.descripcionHecho()));
        preDenuncia.setTipoViolencia(limpiar(request.tipoViolencia()));
        preDenuncia.setFechaIncidente(request.fechaIncidente());
        preDenuncia.setDistrito(limpiar(request.distrito()));
        preDenuncia.setDireccionReferencia(limpiar(request.direccionReferencia()));
        preDenuncia.setAnonima(request.anonima() == null || request.anonima());
        preDenuncia.setEstado(EstadoPreDenuncia.PENDIENTE);
        preDenuncia.setActivo(true);
        preDenuncia.setFechaCreacion(ahora);
        preDenuncia.setFechaActualizacion(ahora);
        PreDenuncia guardada = repository.save(preDenuncia);
        auditar("REGISTRAR_PREDENUNCIA", guardada, "Predenuncia registrada desde formulario inicial");
        return responder(guardada);
    }

    @Transactional(readOnly = true)
    public List<PreDenunciaResponse> listar(EstadoPreDenuncia estado) {
        List<PreDenuncia> preDenuncias = estado == null
                ? repository.findByActivoTrueOrderByFechaCreacionDesc()
                : repository.findByEstadoAndActivoTrueOrderByFechaCreacionDesc(estado);
        return preDenuncias.stream().map(this::responder).toList();
    }

    @Transactional(readOnly = true)
    public PreDenunciaResponse buscarPorId(String id) {
        return responder(obtenerActiva(id));
    }

    @Transactional
    public PreDenunciaResponse marcarEnContacto(String id) {
        PreDenuncia preDenuncia = obtenerActiva(id);
        validarNoDescartada(preDenuncia);
        Usuario actor = obtenerUsuarioAutenticado();
        OffsetDateTime ahora = OffsetDateTime.now();
        preDenuncia.setEstado(EstadoPreDenuncia.EN_CONTACTO);
        preDenuncia.setAsignadaA(actor.getId());
        preDenuncia.setActualizadoPor(actor.getId());
        preDenuncia.setFechaContacto(ahora);
        preDenuncia.setFechaActualizacion(ahora);
        PreDenuncia guardada = repository.save(preDenuncia);
        auditar("CONTACTAR_PREDENUNCIA", guardada, "Predenuncia marcada en contacto");
        return responder(guardada);
    }

    @Transactional
    public PreDenunciaResponse formalizar(String id, FormalizarPreDenunciaRequest request) {
        PreDenuncia preDenuncia = obtenerActiva(id);
        validarNoDescartada(preDenuncia);
        Usuario actor = obtenerUsuarioAutenticado();
        OffsetDateTime ahora = OffsetDateTime.now();
        garantizarAliasActivo(request.victimaId(), actor.getId(), ahora);
        preDenuncia.setEstado(EstadoPreDenuncia.FORMALIZADA);
        preDenuncia.setVictimaId(request.victimaId());
        preDenuncia.setDenunciaId(request.denunciaId());
        preDenuncia.setCasoId(limpiar(request.casoId()));
        preDenuncia.setActualizadoPor(actor.getId());
        preDenuncia.setFechaFormalizacion(ahora);
        preDenuncia.setFechaActualizacion(ahora);
        PreDenuncia guardada = repository.save(preDenuncia);
        auditar("FORMALIZAR_PREDENUNCIA", guardada, "Predenuncia vinculada a denuncia formal");
        return responder(guardada);
    }

    @Transactional
    public PreDenunciaResponse descartar(String id, DescartarPreDenunciaRequest request) {
        PreDenuncia preDenuncia = obtenerActiva(id);
        Usuario actor = obtenerUsuarioAutenticado();
        OffsetDateTime ahora = OffsetDateTime.now();
        preDenuncia.setEstado(EstadoPreDenuncia.DESCARTADA);
        preDenuncia.setMotivoDescarte(limpiar(request.motivoDescarte()));
        preDenuncia.setActualizadoPor(actor.getId());
        preDenuncia.setFechaActualizacion(ahora);
        PreDenuncia guardada = repository.save(preDenuncia);
        auditar("DESCARTAR_PREDENUNCIA", guardada, "Predenuncia descartada");
        return responder(guardada);
    }

    @Transactional
    public void inactivar(String id) {
        PreDenuncia preDenuncia = obtenerActiva(id);
        Usuario actor = obtenerUsuarioAutenticado();
        OffsetDateTime ahora = OffsetDateTime.now();
        preDenuncia.setActivo(false);
        preDenuncia.setInactivadoPor(actor.getId());
        preDenuncia.setFechaInactivacion(ahora);
        preDenuncia.setFechaActualizacion(ahora);
        repository.save(preDenuncia);
        auditar("INACTIVAR_PREDENUNCIA", preDenuncia, "Predenuncia inactivada");
    }

    private PreDenuncia obtenerActiva(String id) {
        return repository.findById(id)
                .filter(PreDenuncia::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Predenuncia no encontrada"));
    }

    private void validarContactoSeguro(String telefono, String correo) {
        if (esBlanco(telefono) && esBlanco(correo)) {
            throw new ExcepcionNegocio("Debe registrar al menos un telefono o correo de contacto seguro");
        }
    }

    private void validarNoDescartada(PreDenuncia preDenuncia) {
        if (preDenuncia.getEstado() == EstadoPreDenuncia.DESCARTADA) {
            throw new ExcepcionNegocio("No se puede modificar una predenuncia descartada");
        }
    }

    private void garantizarAliasActivo(String victimaId, String actorId, OffsetDateTime ahora) {
        if (victimaAliasRepository.findTopByVictimaIdAndActivoTrueOrderByFechaAsignacionDesc(victimaId).isPresent()) {
            return;
        }
        Usuario victima = usuarioRepository.findById(victimaId)
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Victima no encontrada"));
        VictimaAlias alias = new VictimaAlias();
        alias.setId(UUID.randomUUID().toString());
        alias.setVictima(victima);
        alias.setAliasCodigo("VIC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        alias.setActivo(true);
        alias.setFechaAsignacion(ahora);
        alias.setFechaActualizacion(ahora);
        alias.setCreadoPor(actorId);
        victimaAliasRepository.save(alias);
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            throw new ExcepcionNegocio("Usuario no autenticado");
        }
        return usuarioRepository.buscarPorCorreo(auth.getName())
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new ExcepcionNegocio("Usuario no autenticado"));
    }

    private void auditar(String accion, PreDenuncia preDenuncia, String detalle) {
        Usuario actor = usuarioAutenticadoOpcional();
        auditoriaService.registrarAccion(new RegistroAuditoriaInterna(
                "PRE_DENUNCIAS",
                actor == null ? null : actor.getId(),
                actor == null ? null : actor.getRol(),
                accion,
                preDenuncia.getId(),
                null,
                detalle,
                null,
                Map.of("estado", preDenuncia.getEstado().name()),
                null,
                null,
                null
        ));
    }

    private Usuario usuarioAutenticadoOpcional() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return usuarioRepository.buscarPorCorreo(auth.getName()).filter(Usuario::isActivo).orElse(null);
    }

    private PreDenunciaResponse responder(PreDenuncia preDenuncia) {
        return new PreDenunciaResponse(
                preDenuncia.getId(),
                preDenuncia.getNombresContacto(),
                preDenuncia.getApellidosContacto(),
                preDenuncia.getTelefonoContacto(),
                preDenuncia.getCorreoContacto(),
                preDenuncia.getDescripcionHecho(),
                preDenuncia.getTipoViolencia(),
                preDenuncia.getFechaIncidente(),
                preDenuncia.getDistrito(),
                preDenuncia.getDireccionReferencia(),
                preDenuncia.isAnonima(),
                preDenuncia.getEstado(),
                preDenuncia.getMotivoDescarte(),
                preDenuncia.getVictimaId(),
                preDenuncia.getDenunciaId(),
                preDenuncia.getCasoId(),
                preDenuncia.getAsignadaA(),
                preDenuncia.getFechaContacto(),
                preDenuncia.getFechaFormalizacion(),
                preDenuncia.getFechaCreacion(),
                preDenuncia.getFechaActualizacion()
        );
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
