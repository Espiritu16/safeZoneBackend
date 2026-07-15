package com.utp.safezonebackend.asignaciones.service;

import com.utp.safezonebackend.asignaciones.dto.request.ActualizarAsignacionCasoRequest;
import com.utp.safezonebackend.asignaciones.dto.request.CrearAsignacionCasoRequest;
import com.utp.safezonebackend.asignaciones.dto.response.AsignacionCasoResponse;
import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import com.utp.safezonebackend.asignaciones.mapper.AsignacionCasoMapper;
import com.utp.safezonebackend.asignaciones.repository.AsignacionCasoRepository;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.notificaciones.service.NotificacionService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
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
public class AsignacionCasoService {

    private final AsignacionCasoRepository repository;
    private final AsignacionCasoMapper mapper;
    private final CasoRepository casoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public AsignacionCasoService(
            AsignacionCasoRepository repository,
            AsignacionCasoMapper mapper,
            CasoRepository casoRepository,
            UsuarioRepository usuarioRepository,
            NotificacionService notificacionService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.casoRepository = casoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    @Transactional(readOnly = true)
    public List<AsignacionCasoResponse> findAll(String casoId) {
        List<AsignacionCaso> asignaciones = casoId == null || casoId.isBlank()
                ? repository.findByActivoTrueOrderByFechaAsignacionDesc()
                : repository.findByCasoIdAndActivoTrueOrderByFechaAsignacionDesc(casoId.trim());
        asignaciones = limitarAsignacionesPorRol(asignaciones);
        return asignaciones.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AsignacionCasoResponse findById(String id) {
        AsignacionCaso asignacion = obtenerActiva(id);
        validarAccesoAsignacion(asignacion);
        return mapper.toResponse(asignacion);
    }

    @Transactional
    public AsignacionCasoResponse create(CrearAsignacionCasoRequest request) {
        Caso caso = obtenerCasoActivo(request.casoId());
        Usuario profesional = validarProfesional(request.profesionalId(), request.rolProfesional());

        if (repository.existsByCasoIdAndProfesionalIdAndRolProfesionalAndActivoTrue(
                request.casoId().trim(),
                profesional.getId(),
                request.rolProfesional()
        )) {
            throw new ExcepcionNegocio("El profesional ya esta asignado al caso con ese rol");
        }

        OffsetDateTime ahora = OffsetDateTime.now();
        String actorId = obtenerActorId();
        cerrarAsignacionActualDelRol(request.casoId().trim(), request.rolProfesional(), actorId, ahora);

        AsignacionCaso asignacion = new AsignacionCaso();
        asignacion.setId(UUID.randomUUID().toString());
        asignacion.setCasoId(request.casoId().trim());
        asignacion.setProfesionalId(profesional.getId());
        asignacion.setRolProfesional(request.rolProfesional());
        asignacion.setActivo(true);
        asignacion.setFechaAsignacion(ahora);
        asignacion.setFechaActualizacion(ahora);
        asignacion.setAsignadoPor(actorId);
        AsignacionCaso guardada = repository.save(asignacion);
        notificacionService.notificarNuevaAsignacion(guardada.getProfesionalId(), guardada.getCasoId());
        notificacionService.notificarNuevaAsignacionVictima(caso.getVictimaId(), guardada.getCasoId());
        return mapper.toResponse(guardada);
    }

    @Transactional
    public AsignacionCasoResponse update(String id, ActualizarAsignacionCasoRequest request) {
        AsignacionCaso asignacion = obtenerActiva(id);
        RolUsuario rol = request.rolProfesional() == null ? asignacion.getRolProfesional() : request.rolProfesional();
        String profesionalId = request.profesionalId() == null ? asignacion.getProfesionalId() : request.profesionalId();
        Usuario profesional = validarProfesional(profesionalId, rol);

        asignacion.setProfesionalId(profesional.getId());
        asignacion.setRolProfesional(rol);
        asignacion.setFechaActualizacion(OffsetDateTime.now());
        asignacion.setActualizadoPor(obtenerActorId());

        if (Boolean.FALSE.equals(request.activo())) {
            inactivar(asignacion);
        }

        return mapper.toResponse(repository.save(asignacion));
    }

    @Transactional
    public void inactivar(String id) {
        AsignacionCaso asignacion = obtenerActiva(id);
        inactivar(asignacion);
        repository.save(asignacion);
    }

    private AsignacionCaso obtenerActiva(String id) {
        return repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignacion de caso no encontrada"));
    }

    private void validarCasoActivo(String casoId) {
        obtenerCasoActivo(casoId);
    }

    private Caso obtenerCasoActivo(String casoId) {
        if (casoId == null || casoId.isBlank()) {
            throw new ExcepcionNegocio("El caso es obligatorio");
        }
        return casoRepository.findByIdAndActivoTrue(casoId.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Caso no encontrado"));
    }

    private Usuario validarProfesional(String profesionalId, RolUsuario rolProfesional) {
        if (profesionalId == null || profesionalId.isBlank()) {
            throw new ExcepcionNegocio("El profesional es obligatorio");
        }
        validarRolAsignable(rolProfesional);
        Usuario profesional = usuarioRepository.findById(profesionalId.trim())
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesional no encontrado"));
        if (profesional.getRol() != rolProfesional) {
            throw new ExcepcionNegocio("El profesional seleccionado no corresponde al rol " + rolProfesional);
        }
        return profesional;
    }

    private void validarRolAsignable(RolUsuario rolProfesional) {
        if (rolProfesional != RolUsuario.PSICOLOGO && rolProfesional != RolUsuario.DEFENSOR) {
            throw new ExcepcionNegocio("Solo se puede asignar psicologo o defensor legal a un caso");
        }
    }

    private void cerrarAsignacionActualDelRol(String casoId, RolUsuario rolProfesional, String actorId, OffsetDateTime ahora) {
        repository.findTopByCasoIdAndRolProfesionalAndActivoTrueOrderByFechaAsignacionDesc(casoId, rolProfesional)
                .ifPresent(actual -> {
                    actual.setActivo(false);
                    actual.setFechaFin(ahora);
                    actual.setFechaInactivacion(ahora);
                    actual.setFechaActualizacion(ahora);
                    actual.setInactivadoPor(actorId);
                    repository.save(actual);
                });
    }

    private void inactivar(AsignacionCaso asignacion) {
        OffsetDateTime ahora = OffsetDateTime.now();
        asignacion.setActivo(false);
        asignacion.setFechaFin(ahora);
        asignacion.setFechaInactivacion(ahora);
        asignacion.setFechaActualizacion(ahora);
        asignacion.setInactivadoPor(obtenerActorId());
    }

    private List<AsignacionCaso> limitarAsignacionesPorRol(List<AsignacionCaso> asignaciones) {
        Usuario actor = obtenerActorActual();
        if (actor == null || actor.getRol() == RolUsuario.ADMIN || actor.getRol() == RolUsuario.RECEPCIONISTA) {
            return asignaciones;
        }
        if (actor.getRol() == RolUsuario.PSICOLOGO || actor.getRol() == RolUsuario.DEFENSOR) {
            List<String> casoIdsAsignados = repository
                    .findByProfesionalIdAndActivoTrueOrderByFechaAsignacionDesc(actor.getId())
                    .stream()
                    .map(AsignacionCaso::getCasoId)
                    .distinct()
                    .toList();
            return asignaciones.stream().filter(asignacion -> casoIdsAsignados.contains(asignacion.getCasoId())).toList();
        }
        return List.of();
    }

    private void validarAccesoAsignacion(AsignacionCaso asignacion) {
        if (!limitarAsignacionesPorRol(List.of(asignacion)).isEmpty()) {
            return;
        }
        throw new RecursoNoEncontradoException("Asignacion de caso no encontrada");
    }

    private String obtenerActorId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ExcepcionNegocio("No se pudo identificar al usuario autenticado");
        }
        return usuarioRepository.buscarPorCorreo(auth.getName())
                .map(Usuario::getId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario autenticado no encontrado"));
    }

    private Usuario obtenerActorActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return usuarioRepository.buscarPorCorreo(auth.getName()).orElse(null);
    }
}
