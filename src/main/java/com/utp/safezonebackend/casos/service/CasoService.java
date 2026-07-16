package com.utp.safezonebackend.casos.service;

import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import com.utp.safezonebackend.asignaciones.repository.AsignacionCasoRepository;
import com.utp.safezonebackend.casos.dto.request.CrearCasoRequest;
import com.utp.safezonebackend.casos.dto.request.ActualizarCasoRequest;
import com.utp.safezonebackend.casos.dto.response.CasoResponse;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.mapper.CasoMapper;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import com.utp.safezonebackend.notificaciones.service.NotificacionService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CasoService {

    private final CasoRepository repository;
    private final CasoMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final AsignacionCasoRepository asignacionCasoRepository;
    private final VictimaAliasRepository victimaAliasRepository;
    private final DenunciaRepository denunciaRepository;
    private final NotificacionService notificacionService;

    private static final Map<EstadoCaso, EnumSet<EstadoCaso>> TRANSICIONES = new EnumMap<>(EstadoCaso.class);

    static {
        TRANSICIONES.put(EstadoCaso.REGISTRADO, EnumSet.of(EstadoCaso.EN_EVALUACION));
        TRANSICIONES.put(EstadoCaso.EN_EVALUACION, EnumSet.of(EstadoCaso.EN_ATENCION, EstadoCaso.CERRADO));
        TRANSICIONES.put(EstadoCaso.EN_ATENCION, EnumSet.of(EstadoCaso.EN_EVALUACION, EstadoCaso.DERIVADO, EstadoCaso.CERRADO));
        TRANSICIONES.put(EstadoCaso.DERIVADO, EnumSet.of(EstadoCaso.EN_ATENCION, EstadoCaso.CERRADO));
        TRANSICIONES.put(EstadoCaso.CERRADO, EnumSet.of(EstadoCaso.ARCHIVADO));
        TRANSICIONES.put(EstadoCaso.ARCHIVADO, EnumSet.noneOf(EstadoCaso.class));
    }

    public CasoService(
            CasoRepository repository,
            CasoMapper mapper,
            UsuarioRepository usuarioRepository,
            AsignacionCasoRepository asignacionCasoRepository,
            VictimaAliasRepository victimaAliasRepository,
            DenunciaRepository denunciaRepository,
            NotificacionService notificacionService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.asignacionCasoRepository = asignacionCasoRepository;
        this.victimaAliasRepository = victimaAliasRepository;
        this.denunciaRepository = denunciaRepository;
        this.notificacionService = notificacionService;
    }

    @Transactional(readOnly = true)
    public List<CasoResponse> findAll() {
        return limitarCasosPorRol(repository.findByActivoTrueOrderByFechaCreacionDesc()).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CasoResponse> buscar(String victimaId, String aliasCodigo, EstadoCaso estado, com.utp.safezonebackend.casos.enums.PrioridadCaso prioridad, NivelRiesgo nivelRiesgo) {
        List<Caso> casos = repository.findByActivoTrueOrderByFechaCreacionDesc();
        if (victimaId != null && !victimaId.isBlank()) {
            casos = casos.stream().filter(caso -> victimaId.equals(caso.getVictimaId())).toList();
        }
        if (aliasCodigo != null && !aliasCodigo.isBlank()) {
            Set<String> idsVictimasAlias = victimaAliasRepository
                    .findByAliasCodigoContainingIgnoreCaseAndActivoTrue(aliasCodigo.trim())
                    .stream()
                    .map(VictimaAlias::getVictima)
                    .filter(usuario -> usuario != null && usuario.getId() != null)
                    .map(Usuario::getId)
                    .collect(Collectors.toSet());
            casos = casos.stream().filter(caso -> idsVictimasAlias.contains(caso.getVictimaId())).toList();
        }
        if (estado != null) {
            casos = casos.stream().filter(caso -> estado == caso.getEstado()).toList();
        }
        if (prioridad != null) {
            casos = casos.stream().filter(caso -> prioridad == caso.getPrioridad()).toList();
        }
        if (nivelRiesgo != null && !casos.isEmpty()) {
            List<String> casoIds = casos.stream().map(Caso::getId).toList();
            List<String> casosConRiesgo = denunciaRepository
                    .findByCasoIdInAndNivelRiesgoAndActivoTrue(casoIds, nivelRiesgo)
                    .stream()
                    .map(com.utp.safezonebackend.denuncias.entity.Denuncia::getCasoId)
                    .distinct()
                    .toList();
            casos = casos.stream().filter(caso -> casosConRiesgo.contains(caso.getId())).toList();
        }
        casos = limitarCasosPorRol(casos);
        return casos.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CasoResponse findById(String id) {
        Caso caso = obtenerActivo(id);
        validarAccesoCaso(caso);
        return mapper.toResponse(caso);
    }

    @Transactional
    public CasoResponse create(CrearCasoRequest request) {
        validarVictimaActiva(request.victimaId());
        OffsetDateTime ahora = OffsetDateTime.now();
        Caso caso = new Caso();
        caso.setId(UUID.randomUUID().toString());
        caso.setVictimaId(request.victimaId().trim());
        caso.setResumen(limpiar(request.resumen()));
        caso.setDistrito(limpiar(request.distrito()));
        caso.setPrioridad(request.prioridad());
        caso.setEstado(request.estado() == null ? EstadoCaso.REGISTRADO : request.estado());
        caso.setActivo(true);
        caso.setFechaCreacion(ahora);
        caso.setFechaActualizacion(ahora);
        Caso guardado = repository.save(caso);
        return mapper.toResponse(guardado);
    }

    @Transactional
    public CasoResponse update(String id, ActualizarCasoRequest request) {
        Caso caso = obtenerActivo(id);
        validarAccesoCaso(caso);
        EstadoCaso estadoAnterior = caso.getEstado();
        if (request.resumen() != null) {
            caso.setResumen(limpiar(request.resumen()));
        }
        if (request.distrito() != null) {
            caso.setDistrito(limpiar(request.distrito()));
        }
        if (request.prioridad() != null) {
            caso.setPrioridad(request.prioridad());
        }
        if (request.estado() != null && request.estado() != estadoAnterior) {
            validarTransicion(estadoAnterior, request.estado());
            caso.setEstado(request.estado());
            if (request.estado() == EstadoCaso.CERRADO) {
                caso.setFechaCierre(OffsetDateTime.now());
            }
        }
        if (Boolean.FALSE.equals(request.activo())) {
            inactivar(caso);
        }
        caso.setFechaActualizacion(OffsetDateTime.now());
        Caso guardado = repository.save(caso);
        if (request.estado() != null && request.estado() != estadoAnterior) {
            notificacionService.notificarCambioEstadoCaso(guardado.getVictimaId(), guardado.getId(), guardado.getEstado().name());
        }
        return mapper.toResponse(guardado);
    }

    @Transactional
    public void inactivar(String id) {
        Caso caso = obtenerActivo(id);
        validarAccesoCaso(caso);
        inactivar(caso);
        repository.save(caso);
    }

    private Caso obtenerActivo(String id) {
        return repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Caso no encontrado"));
    }

    private void validarVictimaActiva(String victimaId) {
        usuarioRepository.findById(victimaId)
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Victima no encontrada"));
    }

    private List<Caso> limitarCasosPorRol(List<Caso> casos) {
        Usuario actor = obtenerActorActual();
        if (actor == null || actor.getRol() == RolUsuario.ADMIN || actor.getRol() == RolUsuario.RECEPCIONISTA) {
            return casos;
        }
        if (actor.getRol() == RolUsuario.PSICOLOGO || actor.getRol() == RolUsuario.DEFENSOR) {
            List<String> casoIdsAsignados = asignacionCasoRepository
                    .findByProfesionalIdAndActivoTrueOrderByFechaAsignacionDesc(actor.getId())
                    .stream()
                    .map(AsignacionCaso::getCasoId)
                    .distinct()
                    .toList();
            return casos.stream().filter(caso -> casoIdsAsignados.contains(caso.getId())).toList();
        }
        if (actor.getRol() == RolUsuario.VICTIMA) {
            return casos.stream().filter(caso -> actor.getId().equals(caso.getVictimaId())).toList();
        }
        return List.of();
    }

    private void validarAccesoCaso(Caso caso) {
        if (!limitarCasosPorRol(List.of(caso)).isEmpty()) {
            return;
        }
        throw new RecursoNoEncontradoException("Caso no encontrado");
    }

    private Usuario obtenerActorActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return usuarioRepository.buscarPorCorreo(auth.getName()).orElse(null);
    }

    private void validarTransicion(EstadoCaso origen, EstadoCaso destino) {
        if (!TRANSICIONES.getOrDefault(origen, EnumSet.noneOf(EstadoCaso.class)).contains(destino)) {
            throw new ExcepcionNegocio("Transicion de estado no permitida: " + origen + " -> " + destino);
        }
    }

    private void inactivar(Caso caso) {
        caso.setActivo(false);
        caso.setFechaInactivacion(OffsetDateTime.now());
        caso.setFechaActualizacion(OffsetDateTime.now());
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
