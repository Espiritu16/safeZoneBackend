package com.utp.safezonebackend.predenuncias.service;

import com.utp.safezonebackend.auditoria.dto.request.RegistroAuditoriaInterna;
import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.denuncias.dto.request.CrearDenunciaRequest;
import com.utp.safezonebackend.denuncias.dto.response.DenunciaResponse;
import com.utp.safezonebackend.denuncias.service.DenunciaService;
import com.utp.safezonebackend.denuncias.util.TipoViolenciaNormalizer;
import com.utp.safezonebackend.evidencias.service.EvidenciaService;
import com.utp.safezonebackend.notificaciones.service.NotificacionService;
import com.utp.safezonebackend.predenuncias.dto.request.CrearPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.request.DescartarPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.request.FormalizarPreDenunciaRequest;
import com.utp.safezonebackend.predenuncias.dto.response.PreDenunciaResponse;
import com.utp.safezonebackend.predenuncias.entity.PreDenuncia;
import com.utp.safezonebackend.predenuncias.enums.EstadoPreDenuncia;
import com.utp.safezonebackend.predenuncias.repository.PreDenunciaRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.shared.util.DistritoNormalizer;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PreDenunciaService {

    private final PreDenunciaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final VictimaAliasRepository victimaAliasRepository;
    private final AuditoriaService auditoriaService;
    private final DenunciaService denunciaService;
    private final PasswordEncoder passwordEncoder;
    private final EvidenciaService evidenciaService;
    private final NotificacionService notificacionService;

    public PreDenunciaService(
            PreDenunciaRepository repository,
            UsuarioRepository usuarioRepository,
            VictimaAliasRepository victimaAliasRepository,
            AuditoriaService auditoriaService,
            DenunciaService denunciaService,
            PasswordEncoder passwordEncoder,
            EvidenciaService evidenciaService,
            NotificacionService notificacionService
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.victimaAliasRepository = victimaAliasRepository;
        this.auditoriaService = auditoriaService;
        this.denunciaService = denunciaService;
        this.passwordEncoder = passwordEncoder;
        this.evidenciaService = evidenciaService;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public PreDenunciaResponse registrar(CrearPreDenunciaRequest request) {
        validarContactoSeguro(request.telefonoContacto(), request.correoContacto());
        OffsetDateTime ahora = OffsetDateTime.now();
        Usuario actor = usuarioAutenticadoOpcional();
        PreDenuncia preDenuncia = new PreDenuncia();
        preDenuncia.setId(UUID.randomUUID().toString());
        preDenuncia.setNombresContacto(limpiar(request.nombresContacto()));
        preDenuncia.setApellidosContacto(limpiar(request.apellidosContacto()));
        preDenuncia.setTelefonoContacto(limpiar(request.telefonoContacto()));
        preDenuncia.setCorreoContacto(limpiar(request.correoContacto()));
        preDenuncia.setDescripcionHecho(limpiar(request.descripcionHecho()));
        preDenuncia.setTipoViolencia(TipoViolenciaNormalizer.normalizar(request.tipoViolencia()));
        preDenuncia.setFechaIncidente(request.fechaIncidente());
        preDenuncia.setDistrito(DistritoNormalizer.normalizar(request.distrito()));
        preDenuncia.setDireccionReferencia(limpiar(request.direccionReferencia()));
        preDenuncia.setAnonima(request.anonima() == null || request.anonima());
        preDenuncia.setEstado(EstadoPreDenuncia.PENDIENTE);
        preDenuncia.setActivo(true);
        preDenuncia.setFechaCreacion(ahora);
        preDenuncia.setFechaActualizacion(ahora);
        if (actor != null && actor.getRol() == RolUsuario.VICTIMA) {
            preDenuncia.setVictimaId(actor.getId());
            preDenuncia.setCreadoPor(actor.getId());
            preDenuncia.setAnonima(false);
        }
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
    public List<PreDenunciaResponse> listarMisRegistros() {
        Usuario actor = obtenerUsuarioAutenticado();
        if (actor.getRol() != RolUsuario.VICTIMA) {
            throw new ExcepcionNegocio("Solo una victima puede consultar sus predenuncias");
        }
        return repository.findByVictimaIdAndActivoTrueOrderByFechaCreacionDesc(actor.getId()).stream()
                .map(this::responder)
                .toList();
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
        notificacionService.notificarPredenunciaEnContacto(guardada.getVictimaId(), guardada.getId());
        return responder(guardada);
    }

    @Transactional
    public PreDenunciaResponse formalizar(String id, FormalizarPreDenunciaRequest request) {
        PreDenuncia preDenuncia = obtenerActiva(id);
        validarNoDescartada(preDenuncia);
        validarEnContacto(preDenuncia);
        Usuario actor = obtenerUsuarioAutenticado();
        OffsetDateTime ahora = OffsetDateTime.now();
        validarDatosFormalizacion(request);
        String victimaId = resolverVictimaFormalizacion(preDenuncia, request, actor, ahora);
        garantizarAliasActivo(victimaId, actor.getId(), ahora);
        DenunciaFormalizada denuncia = resolverDenunciaFormal(preDenuncia, request, victimaId);
        preDenuncia.setEstado(EstadoPreDenuncia.FORMALIZADA);
        preDenuncia.setVictimaId(victimaId);
        preDenuncia.setDenunciaId(denuncia.denunciaId());
        preDenuncia.setCasoId(denuncia.casoId());
        preDenuncia.setActualizadoPor(actor.getId());
        preDenuncia.setFechaFormalizacion(ahora);
        preDenuncia.setFechaActualizacion(ahora);
        PreDenuncia guardada = repository.save(preDenuncia);
        evidenciaService.vincularPredenunciaAFormalizacion(guardada.getId(), guardada.getCasoId(), guardada.getDenunciaId());
        auditar("FORMALIZAR_PREDENUNCIA", guardada, "Predenuncia vinculada a denuncia formal");
        notificacionService.notificarPredenunciaFormalizada(guardada.getVictimaId(), guardada.getCasoId(), guardada.getDenunciaId());
        return responder(guardada);
    }

    private void validarDatosFormalizacion(FormalizarPreDenunciaRequest request) {
        if (request.nivelRiesgo() == null) {
            throw new ExcepcionNegocio("Debe indicar el nivel de riesgo para crear la denuncia formal");
        }
        validarEdadFormalizacion(request.edad());
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

    private void validarEnContacto(PreDenuncia preDenuncia) {
        if (preDenuncia.getEstado() != EstadoPreDenuncia.EN_CONTACTO) {
            throw new ExcepcionNegocio("Debe marcar la predenuncia en contacto antes de formalizarla");
        }
    }

    private String resolverVictimaFormalizacion(
            PreDenuncia preDenuncia,
            FormalizarPreDenunciaRequest request,
            Usuario actor,
            OffsetDateTime ahora
    ) {
        if (Boolean.TRUE.equals(request.formalizarAnonima())) {
            if (!esBlanco(preDenuncia.getVictimaId())) {
                return preDenuncia.getVictimaId();
            }
            return crearVictimaProtegida(preDenuncia, actor, ahora).getId();
        }
        if (esBlanco(request.victimaId())) {
            throw new ExcepcionNegocio("Debe indicar la victima para formalizar la predenuncia");
        }
        return request.victimaId().trim();
    }

    private Usuario crearVictimaProtegida(PreDenuncia preDenuncia, Usuario actor, OffsetDateTime ahora) {
        String dniTecnico = generarDniTecnicoAlias();
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID().toString());
        usuario.setCorreo("alias." + dniTecnico.toLowerCase() + "@safezone.local");
        usuario.setContrasenaHash(passwordEncoder.encode(UUID.randomUUID().toString()));
        usuario.setNombres("Victima protegida");
        usuario.setApellidos("Alias anonimo");
        usuario.setDni(dniTecnico);
        usuario.setTelefono(limpiar(preDenuncia.getTelefonoContacto()));
        usuario.setDistrito(DistritoNormalizer.normalizar(preDenuncia.getDistrito()));
        usuario.setRol(RolUsuario.VICTIMA);
        usuario.setActivo(true);
        usuario.setCreadoPor(actor.getId());
        usuario.setFechaCreacion(ahora);
        usuario.setFechaActualizacion(ahora);
        return usuarioRepository.save(usuario);
    }

    private String generarDniTecnicoAlias() {
        String dni;
        do {
            dni = "ALIAS" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        } while (usuarioRepository.existsByDni(dni));
        return dni;
    }

    private DenunciaFormalizada resolverDenunciaFormal(
            PreDenuncia preDenuncia,
            FormalizarPreDenunciaRequest request,
            String victimaId
    ) {
        if (!esBlanco(request.denunciaId())) {
            return new DenunciaFormalizada(limpiar(request.denunciaId()), limpiar(request.casoId()));
        }
        DenunciaResponse denuncia = denunciaService.create(new CrearDenunciaRequest(
                limpiar(request.casoId()),
                victimaId,
                preDenuncia.getDescripcionHecho(),
                preDenuncia.getTipoViolencia(),
                preDenuncia.getFechaIncidente(),
                preDenuncia.getDistrito(),
                preDenuncia.getDireccionReferencia(),
                request.nivelRiesgo(),
                Boolean.TRUE.equals(request.formalizarAnonima()) || preDenuncia.isAnonima(),
                null,
                request.edad()
        ));
        return new DenunciaFormalizada(denuncia.id(), denuncia.casoId());
    }

    private void validarEdadFormalizacion(Integer edad) {
        if (edad == null || edad <= 0 || edad > 120) {
            throw new ExcepcionNegocio("Debe indicar una edad valida para formalizar la denuncia");
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

    private record DenunciaFormalizada(String denunciaId, String casoId) {
    }
}
