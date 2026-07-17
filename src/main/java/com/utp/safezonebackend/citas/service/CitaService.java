package com.utp.safezonebackend.citas.service;

import com.utp.safezonebackend.citas.dto.request.CrearCitaRequest;
import com.utp.safezonebackend.citas.dto.request.ActualizarCitaRequest;
import com.utp.safezonebackend.citas.dto.response.CitaResponse;
import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import com.utp.safezonebackend.asignaciones.repository.AsignacionCasoRepository;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.citas.entity.Cita;
import com.utp.safezonebackend.citas.enums.EstadoCita;
import com.utp.safezonebackend.citas.enums.TipoCita;
import com.utp.safezonebackend.citas.mapper.CitaMapper;
import com.utp.safezonebackend.citas.repository.CitaRepository;
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
public class CitaService {

    private final CitaRepository repository;
    private final CitaMapper mapper;
    private final CasoRepository casoRepository;
    private final AsignacionCasoRepository asignacionCasoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public CitaService(
            CitaRepository repository,
            CitaMapper mapper,
            CasoRepository casoRepository,
            AsignacionCasoRepository asignacionCasoRepository,
            UsuarioRepository usuarioRepository,
            NotificacionService notificacionService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.casoRepository = casoRepository;
        this.asignacionCasoRepository = asignacionCasoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> findAll() {
        return listarCitasVisibles().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CitaResponse findById(String id) {
        Cita cita = obtenerActiva(id);
        validarAccesoCita(cita);
        return mapper.toResponse(cita);
    }

    @Transactional
    public CitaResponse create(CrearCitaRequest request) {
        Caso caso = casoRepository.findByIdAndActivoTrue(request.casoId().trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Caso no encontrado"));
        RolUsuario rolProfesional = rolDesdeTipo(request.tipoCita());
        AsignacionCaso asignacion = asignacionCasoRepository
                .findTopByCasoIdAndRolProfesionalAndActivoTrueOrderByFechaAsignacionDesc(caso.getId(), rolProfesional)
                .orElseThrow(() -> new ExcepcionNegocio("El caso no tiene profesional asignado para " + request.tipoCita()));
        OffsetDateTime fechaInicio = request.fechaInicio();
        OffsetDateTime fechaFin = fechaFin(request.fechaInicio(), request.fechaFin());
        validarRango(fechaInicio, fechaFin);
        validarDisponibilidad(asignacion.getProfesionalId(), fechaInicio, fechaFin, null);

        OffsetDateTime ahora = OffsetDateTime.now();
        Cita cita = new Cita();
        cita.setId(UUID.randomUUID().toString());
        cita.setCasoId(caso.getId());
        cita.setVictimaId(caso.getVictimaId());
        cita.setEspecialistaId(asignacion.getProfesionalId());
        cita.setTipoCita(request.tipoCita());
        cita.setFechaInicio(fechaInicio);
        cita.setFechaFin(fechaFin);
        cita.setEstado(EstadoCita.PROGRAMADA);
        cita.setReprogramada(false);
        cita.setObservaciones(limpiar(request.observaciones()));
        cita.setActivo(true);
        cita.setFechaCreacion(ahora);
        cita.setFechaActualizacion(ahora);
        Cita guardada = repository.save(cita);
        notificacionService.notificarCitaProxima(guardada);
        return mapper.toResponse(guardada);
    }

    @Transactional
    public CitaResponse update(String id, ActualizarCitaRequest request) {
        Cita cita = obtenerActiva(id);
        validarAccesoCita(cita);
        TipoCita tipo = request.tipoCita() == null ? cita.getTipoCita() : request.tipoCita();
        OffsetDateTime fechaInicio = request.fechaInicio() == null ? cita.getFechaInicio() : request.fechaInicio();
        OffsetDateTime fechaFin = fechaFin(fechaInicio, request.fechaFin() == null ? cita.getFechaFin() : request.fechaFin());
        boolean reprogramada = request.fechaInicio() != null || request.fechaFin() != null || request.tipoCita() != null;
        if (reprogramada) {
            AsignacionCaso asignacion = asignacionCasoRepository
                    .findTopByCasoIdAndRolProfesionalAndActivoTrueOrderByFechaAsignacionDesc(cita.getCasoId(), rolDesdeTipo(tipo))
                    .orElseThrow(() -> new ExcepcionNegocio("El caso no tiene profesional asignado para " + tipo));
            validarRango(fechaInicio, fechaFin);
            validarDisponibilidad(asignacion.getProfesionalId(), fechaInicio, fechaFin, cita.getId());
            cita.setEspecialistaId(asignacion.getProfesionalId());
            cita.setTipoCita(tipo);
            cita.setFechaInicio(fechaInicio);
            cita.setFechaFin(fechaFin);
            cita.setEstado(EstadoCita.PROGRAMADA);
            cita.setReprogramada(true);
        }
        if (request.estado() != null) {
            cita.setEstado(request.estado());
        }
        if (request.motivoCancelacion() != null) {
            cita.setMotivoCancelacion(limpiar(request.motivoCancelacion()));
        }
        if (request.observaciones() != null) {
            cita.setObservaciones(limpiar(request.observaciones()));
        }
        cita.setFechaActualizacion(OffsetDateTime.now());
        Cita guardada = repository.save(cita);
        if (reprogramada) {
            notificacionService.notificarCitaProxima(guardada);
        }
        return mapper.toResponse(guardada);
    }

    @Transactional
    public void inactivar(String id) {
        Cita cita = obtenerActiva(id);
        validarAccesoCita(cita);
        cita.setActivo(false);
        cita.setEstado(EstadoCita.CANCELADA);
        cita.setFechaInactivacion(OffsetDateTime.now());
        cita.setFechaActualizacion(OffsetDateTime.now());
        repository.save(cita);
    }

    private Cita obtenerActiva(String id) {
        return repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada"));
    }

    private List<Cita> listarCitasVisibles() {
        Usuario actor = obtenerActorActual();
        if (actor == null || actor.getRol() == RolUsuario.ADMIN || actor.getRol() == RolUsuario.RECEPCIONISTA) {
            return repository.findByActivoTrueOrderByFechaInicioDesc();
        }
        if (actor.getRol() == RolUsuario.PSICOLOGO || actor.getRol() == RolUsuario.DEFENSOR) {
            return repository.findByEspecialistaIdAndActivoTrueOrderByFechaInicioDesc(actor.getId());
        }
        if (actor.getRol() == RolUsuario.VICTIMA) {
            return repository.findByVictimaIdAndActivoTrue(actor.getId());
        }
        return List.of();
    }

    private void validarAccesoCita(Cita cita) {
        Usuario actor = obtenerActorActual();
        if (actor == null || actor.getRol() == RolUsuario.ADMIN || actor.getRol() == RolUsuario.RECEPCIONISTA) {
            return;
        }
        if ((actor.getRol() == RolUsuario.PSICOLOGO || actor.getRol() == RolUsuario.DEFENSOR)
                && actor.getId().equals(cita.getEspecialistaId())) {
            return;
        }
        if (actor.getRol() == RolUsuario.VICTIMA && actor.getId().equals(cita.getVictimaId())) {
            return;
        }
        throw new RecursoNoEncontradoException("Cita no encontrada");
    }

    private Usuario obtenerActorActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return usuarioRepository.buscarPorCorreo(auth.getName()).orElse(null);
    }

    private void validarDisponibilidad(String especialistaId, OffsetDateTime fechaInicio, OffsetDateTime fechaFin, String citaActualId) {
        List<Cita> solapadas = repository.findSolapadasPorEspecialista(
                especialistaId,
                fechaInicio,
                fechaFin,
                List.of(EstadoCita.CANCELADA)
        );
        boolean tieneCruce = solapadas.stream().anyMatch(cita -> !cita.getId().equals(citaActualId));
        if (tieneCruce) {
            throw new ExcepcionNegocio("El profesional asignado no tiene disponibilidad en ese horario");
        }
    }

    private void validarRango(OffsetDateTime fechaInicio, OffsetDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null || !fechaFin.isAfter(fechaInicio)) {
            throw new ExcepcionNegocio("La fecha de fin debe ser posterior a la fecha de inicio");
        }
    }

    private OffsetDateTime fechaFin(OffsetDateTime fechaInicio, OffsetDateTime fechaFin) {
        if (fechaFin != null) {
            return fechaFin;
        }
        return fechaInicio == null ? null : fechaInicio.plusMinutes(60);
    }

    private RolUsuario rolDesdeTipo(TipoCita tipoCita) {
        return switch (tipoCita) {
            case PSICOLOGIA -> RolUsuario.PSICOLOGO;
            case LEGAL -> RolUsuario.DEFENSOR;
        };
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
