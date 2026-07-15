package com.utp.safezonebackend.notificaciones.service;

import com.utp.safezonebackend.notificaciones.dto.request.CrearNotificacionRequest;
import com.utp.safezonebackend.notificaciones.dto.request.ActualizarNotificacionRequest;
import com.utp.safezonebackend.notificaciones.dto.response.NotificacionResponse;
import com.utp.safezonebackend.citas.entity.Cita;
import com.utp.safezonebackend.notificaciones.mapper.NotificacionMapper;
import com.utp.safezonebackend.notificaciones.entity.Notificacion;
import com.utp.safezonebackend.notificaciones.enums.PrioridadNotificacion;
import com.utp.safezonebackend.notificaciones.enums.TipoNotificacion;
import com.utp.safezonebackend.notificaciones.repository.NotificacionRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacionService {

    private final NotificacionRepository repository;
    private final NotificacionMapper mapper;
    private final UsuarioRepository usuarioRepository;

    public NotificacionService(
            NotificacionRepository repository,
            NotificacionMapper mapper,
            UsuarioRepository usuarioRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> findAll() {
        return repository.findByActivoTrueOrderByFechaCreacionDesc().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> findAll(String usuarioId) {
        List<Notificacion> notificaciones = usuarioId == null || usuarioId.isBlank()
                ? repository.findByActivoTrueOrderByFechaCreacionDesc()
                : repository.findByUsuarioIdAndActivoTrueOrderByFechaCreacionDesc(usuarioId.trim());
        return notificaciones.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> findAllAutenticado() {
        Usuario usuario = obtenerUsuarioAutenticado();
        return repository.findByUsuarioIdAndActivoTrueOrderByFechaCreacionDesc(usuario.getId()).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public NotificacionResponse findById(String id) {
        return mapper.toResponse(obtenerActiva(id));
    }

    @Transactional
    public NotificacionResponse create(CrearNotificacionRequest request) {
        return mapper.toResponse(repository.save(nueva(
                request.usuarioId(),
                request.casoId(),
                request.denunciaId(),
                request.tipo(),
                request.prioridad(),
                request.titulo(),
                request.mensaje()
        )));
    }

    @Transactional
    public NotificacionResponse update(String id, ActualizarNotificacionRequest request) {
        Notificacion notificacion = obtenerActiva(id);
        if (request.prioridad() != null) {
            notificacion.setPrioridad(request.prioridad());
        }
        if (request.titulo() != null) {
            notificacion.setTitulo(limpiar(request.titulo()));
        }
        if (request.mensaje() != null) {
            notificacion.setMensaje(limpiar(request.mensaje()));
        }
        if (request.leida() != null) {
            notificacion.setLeida(request.leida());
            notificacion.setFechaLectura(request.leida() ? OffsetDateTime.now() : null);
        }
        if (Boolean.FALSE.equals(request.activo())) {
            inactivar(notificacion);
        }
        notificacion.setFechaActualizacion(OffsetDateTime.now());
        return mapper.toResponse(repository.save(notificacion));
    }

    @Transactional
    public void inactivar(String id) {
        Notificacion notificacion = obtenerActiva(id);
        inactivar(notificacion);
        repository.save(notificacion);
    }

    @Transactional
    public void notificarNuevaAsignacion(String usuarioId, String casoId) {
        repository.save(nueva(
                usuarioId,
                casoId,
                null,
                TipoNotificacion.SISTEMA,
                PrioridadNotificacion.ALTA,
                "Nueva asignacion de caso",
                "Se te asigno un nuevo caso para seguimiento."
        ));
    }

    @Transactional
    public void notificarNuevaAsignacionVictima(String usuarioId, String casoId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            return;
        }
        repository.save(nueva(
                usuarioId,
                casoId,
                null,
                TipoNotificacion.SISTEMA,
                PrioridadNotificacion.MEDIA,
                "Profesional asignado a tu caso",
                "El equipo de atencion asigno un profesional para dar seguimiento a tu caso."
        ));
    }

    @Transactional
    public void notificarCambioEstadoCaso(String usuarioId, String casoId, String estado) {
        repository.save(nueva(
                usuarioId,
                casoId,
                null,
                TipoNotificacion.SISTEMA,
                PrioridadNotificacion.MEDIA,
                "Cambio de estado del caso",
                "El caso cambio al estado " + estado + "."
        ));
    }

    @Transactional
    public void notificarPredenunciaEnContacto(String usuarioId, String predenunciaId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            return;
        }
        repository.save(nueva(
                usuarioId,
                null,
                null,
                TipoNotificacion.SISTEMA,
                PrioridadNotificacion.MEDIA,
                "Tu denuncia esta en contacto",
                "El equipo de atencion inicio el contacto para revisar tu registro " + predenunciaId + "."
        ));
    }

    @Transactional
    public void notificarPredenunciaFormalizada(String usuarioId, String casoId, String denunciaId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            return;
        }
        repository.save(nueva(
                usuarioId,
                casoId,
                denunciaId,
                TipoNotificacion.SISTEMA,
                PrioridadNotificacion.ALTA,
                "Tu denuncia fue formalizada",
                "Tu registro ya fue formalizado y vinculado a un caso de atencion."
        ));
    }

    @Transactional
    public void notificarCitaProxima(Cita cita) {
        String titulo = "Cita programada";
        String mensaje = "Se programo una cita " + cita.getTipoCita() + " para " + cita.getFechaInicio() + ".";
        repository.save(nueva(
                cita.getVictimaId(),
                cita.getCasoId(),
                null,
                TipoNotificacion.RECORDATORIO,
                PrioridadNotificacion.MEDIA,
                titulo,
                mensaje
        ));
        repository.save(nueva(
                cita.getEspecialistaId(),
                cita.getCasoId(),
                null,
                TipoNotificacion.RECORDATORIO,
                PrioridadNotificacion.MEDIA,
                titulo,
                mensaje
        ));
    }

    private Notificacion nueva(
            String usuarioId,
            String casoId,
            String denunciaId,
            TipoNotificacion tipo,
            PrioridadNotificacion prioridad,
            String titulo,
            String mensaje
    ) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new ExcepcionNegocio("El usuario destino es obligatorio");
        }
        OffsetDateTime ahora = OffsetDateTime.now();
        Notificacion notificacion = new Notificacion();
        notificacion.setId(UUID.randomUUID().toString());
        notificacion.setUsuarioId(usuarioId.trim());
        notificacion.setCasoId(limpiar(casoId));
        notificacion.setDenunciaId(limpiar(denunciaId));
        notificacion.setTipo(tipo == null ? TipoNotificacion.SISTEMA : tipo);
        notificacion.setPrioridad(prioridad == null ? PrioridadNotificacion.MEDIA : prioridad);
        notificacion.setTitulo(limpiar(titulo));
        notificacion.setMensaje(limpiar(mensaje));
        notificacion.setLeida(false);
        notificacion.setActivo(true);
        notificacion.setFechaCreacion(ahora);
        notificacion.setFechaActualizacion(ahora);
        return notificacion;
    }

    private Notificacion obtenerActiva(String id) {
        return repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificacion no encontrada"));
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

    private void inactivar(Notificacion notificacion) {
        notificacion.setActivo(false);
        notificacion.setFechaInactivacion(OffsetDateTime.now());
        notificacion.setFechaActualizacion(OffsetDateTime.now());
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
