package com.utp.safezonebackend.denuncias.service;

import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import com.utp.safezonebackend.asignaciones.repository.AsignacionCasoRepository;
import com.utp.safezonebackend.denuncias.dto.request.CrearDenunciaRequest;
import com.utp.safezonebackend.denuncias.dto.request.ActualizarDenunciaRequest;
import com.utp.safezonebackend.denuncias.dto.response.DenunciaResponse;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.denuncias.entity.Denuncia;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import com.utp.safezonebackend.denuncias.mapper.DenunciaMapper;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import com.utp.safezonebackend.evidencias.service.EvidenciaService;
import com.utp.safezonebackend.notificaciones.entity.Notificacion;
import com.utp.safezonebackend.notificaciones.enums.PrioridadNotificacion;
import com.utp.safezonebackend.notificaciones.enums.TipoNotificacion;
import com.utp.safezonebackend.notificaciones.repository.NotificacionRepository;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DenunciaService {

    private final DenunciaRepository repository;
    private final DenunciaMapper mapper;
    private final CasoRepository casoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsignacionCasoRepository asignacionCasoRepository;
    private final NotificacionRepository notificacionRepository;
    private final EvidenciaService evidenciaService;
    public DenunciaService(
            DenunciaRepository repository,
            DenunciaMapper mapper,
            CasoRepository casoRepository,
            UsuarioRepository usuarioRepository,
            AsignacionCasoRepository asignacionCasoRepository,
            NotificacionRepository notificacionRepository,
            EvidenciaService evidenciaService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.casoRepository = casoRepository;
        this.usuarioRepository = usuarioRepository;
        this.asignacionCasoRepository = asignacionCasoRepository;
        this.notificacionRepository = notificacionRepository;
        this.evidenciaService=evidenciaService;
    }

    @Transactional(readOnly = true)
    public List<DenunciaResponse> findAll() {
        return limitarDenunciasPorRol(repository.findByActivoTrueOrderByFechaCreacionDesc()).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DenunciaResponse> buscar(String victimaId, String casoId, NivelRiesgo nivelRiesgo, String distrito, String tipoViolencia) {
        return limitarDenunciasPorRol(repository.findByActivoTrueOrderByFechaCreacionDesc()).stream()
                .filter(denuncia -> victimaId == null || victimaId.isBlank() || victimaId.equals(denuncia.getVictimaId()))
                .filter(denuncia -> casoId == null || casoId.isBlank() || casoId.equals(denuncia.getCasoId()))
                .filter(denuncia -> nivelRiesgo == null || nivelRiesgo == denuncia.getNivelRiesgo())
                .filter(denuncia -> distrito == null || distrito.isBlank() || distrito.equalsIgnoreCase(denuncia.getDistrito()))
                .filter(denuncia -> tipoViolencia == null || tipoViolencia.isBlank() || tipoViolencia.equalsIgnoreCase(denuncia.getTipoViolencia()))
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DenunciaResponse findById(String id) {
        Denuncia denuncia = obtenerActiva(id);
        validarAccesoDenuncia(denuncia);
        return mapper.toResponse(denuncia);
    }

    @Transactional
    public DenunciaResponse create(CrearDenunciaRequest request) {
        validarVictimaActiva(request.victimaId());
        OffsetDateTime ahora = OffsetDateTime.now();
        String casoId = request.casoId();
        if (casoId == null || casoId.isBlank()) {
            casoId = crearCasoParaDenuncia(request, ahora);
        } else {
            validarCasoActivo(casoId);
        }

        Denuncia denuncia = new Denuncia();
        denuncia.setId(UUID.randomUUID().toString());
        denuncia.setCasoId(casoId);
        denuncia.setVictimaId(request.victimaId().trim());
        denuncia.setDescripcion(limpiar(request.descripcion()));
        denuncia.setTipoViolencia(limpiar(request.tipoViolencia()));
        denuncia.setFechaIncidente(request.fechaIncidente());
        denuncia.setDistrito(limpiar(request.distrito()));
        denuncia.setDireccionReferencia(limpiar(request.direccionReferencia()));
        denuncia.setNivelRiesgo(request.nivelRiesgo());
        denuncia.setAnonima(request.anonima() == null || request.anonima());
        denuncia.setAdjuntos(request.adjuntos());
        denuncia.setActivo(true);
        denuncia.setEdad(request.edad());
        denuncia.setFechaCreacion(ahora);
        denuncia.setFechaActualizacion(ahora);
        Denuncia guardada = repository.save(denuncia);
        if (request.adjuntos() != null && !request.adjuntos().isEmpty()) {
            evidenciaService.vincularAEvidencias(request.adjuntos(), casoId, guardada.getId());
        }
        notificarSiCritica(guardada);
        return mapper.toResponse(guardada);
    }

    @Transactional
    public DenunciaResponse update(String id, ActualizarDenunciaRequest request) {
        Denuncia denuncia = obtenerActiva(id);
        validarAccesoDenuncia(denuncia);
        NivelRiesgo riesgoAnterior = denuncia.getNivelRiesgo();
        if (request.casoId() != null) {
            validarCasoActivo(request.casoId());
            denuncia.setCasoId(request.casoId().trim());
        }
        if (request.descripcion() != null) {
            denuncia.setDescripcion(limpiar(request.descripcion()));
        }
        if (request.tipoViolencia() != null) {
            denuncia.setTipoViolencia(limpiar(request.tipoViolencia()));
        }
        if (request.fechaIncidente() != null) {
            denuncia.setFechaIncidente(request.fechaIncidente());
        }
        if (request.distrito() != null) {
            denuncia.setDistrito(limpiar(request.distrito()));
        }
        if (request.direccionReferencia() != null) {
            denuncia.setDireccionReferencia(limpiar(request.direccionReferencia()));
        }
        if (request.nivelRiesgo() != null) {
            denuncia.setNivelRiesgo(request.nivelRiesgo());
        }
        if (request.anonima() != null) {
            denuncia.setAnonima(request.anonima());
        }
        if (request.adjuntos() != null) {
            denuncia.setAdjuntos(request.adjuntos());
        }
        denuncia.setFechaActualizacion(OffsetDateTime.now());
        Denuncia guardada = repository.save(denuncia);
        if (riesgoAnterior != NivelRiesgo.CRITICO && guardada.getNivelRiesgo() == NivelRiesgo.CRITICO) {
            notificarSiCritica(guardada);
        }
        return mapper.toResponse(guardada);
    }

    @Transactional
    public void inactivar(String id) {
        Denuncia denuncia = obtenerActiva(id);
        validarAccesoDenuncia(denuncia);
        denuncia.setActivo(false);
        denuncia.setFechaInactivacion(OffsetDateTime.now());
        denuncia.setFechaActualizacion(OffsetDateTime.now());
        repository.save(denuncia);
    }

    private Denuncia obtenerActiva(String id) {
        return repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Denuncia no encontrada"));
    }

    private void validarVictimaActiva(String victimaId) {
        usuarioRepository.findById(victimaId)
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Victima no encontrada"));
    }

    private void validarCasoActivo(String casoId) {
        casoRepository.findByIdAndActivoTrue(casoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Caso no encontrado"));
    }

    private List<Denuncia> limitarDenunciasPorRol(List<Denuncia> denuncias) {
        Usuario actor = obtenerActorActual();
        if (actor == null || actor.getRol() == RolUsuario.ADMIN || actor.getRol() == RolUsuario.RECEPCIONISTA) {
            return denuncias;
        }
        if (actor.getRol() == RolUsuario.PSICOLOGO || actor.getRol() == RolUsuario.DEFENSOR) {
            List<String> casoIdsAsignados = asignacionCasoRepository
                    .findByProfesionalIdAndActivoTrueOrderByFechaAsignacionDesc(actor.getId())
                    .stream()
                    .map(AsignacionCaso::getCasoId)
                    .distinct()
                    .toList();
            return denuncias.stream().filter(denuncia -> casoIdsAsignados.contains(denuncia.getCasoId())).toList();
        }
        if (actor.getRol() == RolUsuario.VICTIMA) {
            return denuncias.stream().filter(denuncia -> actor.getId().equals(denuncia.getVictimaId())).toList();
        }
        return List.of();
    }

    private void validarAccesoDenuncia(Denuncia denuncia) {
        if (!limitarDenunciasPorRol(List.of(denuncia)).isEmpty()) {
            return;
        }
        throw new RecursoNoEncontradoException("Denuncia no encontrada");
    }

    private Usuario obtenerActorActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return usuarioRepository.buscarPorCorreo(auth.getName()).orElse(null);
    }

    private String crearCasoParaDenuncia(CrearDenunciaRequest request, OffsetDateTime ahora) {
        Caso caso = new Caso();
        caso.setId(UUID.randomUUID().toString());
        caso.setVictimaId(request.victimaId().trim());
        caso.setEstado(EstadoCaso.REGISTRADO);
        caso.setPrioridad(prioridadDesdeRiesgo(request.nivelRiesgo()));
        caso.setResumen(resumenCaso(request));
        caso.setDistrito(limpiar(request.distrito()));
        caso.setActivo(true);
        caso.setFechaCreacion(ahora);
        caso.setFechaActualizacion(ahora);
        return casoRepository.save(caso).getId();
    }

    private PrioridadCaso prioridadDesdeRiesgo(NivelRiesgo riesgo) {
        return switch (riesgo) {
            case BAJO -> PrioridadCaso.BAJA;
            case MEDIO -> PrioridadCaso.MEDIA;
            case ALTO -> PrioridadCaso.ALTA;
            case CRITICO -> PrioridadCaso.CRITICA;
        };
    }

    private String resumenCaso(CrearDenunciaRequest request) {
        String tipo = request.tipoViolencia() == null ? "violencia" : request.tipoViolencia().trim();
        return "Caso generado por denuncia de " + tipo;
    }

    private void notificarSiCritica(Denuncia denuncia) {
        if (denuncia.getNivelRiesgo() != NivelRiesgo.CRITICO) {
            return;
        }
        Notificacion notificacion = new Notificacion();
        notificacion.setId(UUID.randomUUID().toString());
        notificacion.setUsuarioId(denuncia.getVictimaId());
        notificacion.setCasoId(denuncia.getCasoId());
        notificacion.setDenunciaId(denuncia.getId());
        notificacion.setTipo(TipoNotificacion.RIESGO_CRITICO);
        notificacion.setPrioridad(PrioridadNotificacion.CRITICA);
        notificacion.setTitulo("Denuncia de riesgo critico");
        notificacion.setMensaje("Se registro una denuncia clasificada como riesgo critico.");
        notificacion.setLeida(false);
        notificacion.setActivo(true);
        notificacion.setFechaCreacion(OffsetDateTime.now());
        notificacion.setFechaActualizacion(OffsetDateTime.now());
        notificacionRepository.save(notificacion);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
