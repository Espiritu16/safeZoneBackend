package com.utp.safezonebackend.casos.service;

import com.utp.safezonebackend.casos.dto.request.CrearCasoRequest;
import com.utp.safezonebackend.casos.dto.request.ActualizarCasoRequest;
import com.utp.safezonebackend.casos.dto.response.CasoResponse;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.mapper.CasoMapper;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CasoService {

    private final CasoRepository repository;
    private final CasoMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final VictimaAliasRepository victimaAliasRepository;
    private final DenunciaRepository denunciaRepository;

    private static final Map<EstadoCaso, EnumSet<EstadoCaso>> TRANSICIONES = new EnumMap<>(EstadoCaso.class);

    static {
        TRANSICIONES.put(EstadoCaso.REGISTRADO, EnumSet.of(EstadoCaso.EN_EVALUACION));
        TRANSICIONES.put(EstadoCaso.EN_EVALUACION, EnumSet.of(EstadoCaso.EN_ATENCION));
        TRANSICIONES.put(EstadoCaso.EN_ATENCION, EnumSet.of(EstadoCaso.DERIVADO, EstadoCaso.CERRADO));
        TRANSICIONES.put(EstadoCaso.DERIVADO, EnumSet.of(EstadoCaso.EN_ATENCION));
        TRANSICIONES.put(EstadoCaso.CERRADO, EnumSet.of(EstadoCaso.ARCHIVADO));
        TRANSICIONES.put(EstadoCaso.ARCHIVADO, EnumSet.noneOf(EstadoCaso.class));
    }

    public CasoService(
            CasoRepository repository,
            CasoMapper mapper,
            UsuarioRepository usuarioRepository,
            VictimaAliasRepository victimaAliasRepository,
            DenunciaRepository denunciaRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.victimaAliasRepository = victimaAliasRepository;
        this.denunciaRepository = denunciaRepository;
    }

    @Transactional(readOnly = true)
    public List<CasoResponse> findAll() {
        return repository.findByActivoTrueOrderByFechaCreacionDesc().stream()
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
            String idVictimaAlias = victimaAliasRepository
                    .findTopByAliasCodigoIgnoreCaseAndActivoTrueOrderByFechaAsignacionDesc(aliasCodigo.trim())
                    .map(VictimaAlias::getVictima)
                    .map(Usuario::getId)
                    .orElse("__sin_resultados__");
            casos = casos.stream().filter(caso -> idVictimaAlias.equals(caso.getVictimaId())).toList();
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
        return casos.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CasoResponse findById(String id) {
        return mapper.toResponse(obtenerActivo(id));
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
        return mapper.toResponse(repository.save(caso));
    }

    @Transactional
    public void inactivar(String id) {
        Caso caso = obtenerActivo(id);
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
