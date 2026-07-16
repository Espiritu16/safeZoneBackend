package com.utp.safezonebackend.usuarios.service;

import com.utp.safezonebackend.auditoria.dto.request.RegistroAuditoriaInterna;
import com.utp.safezonebackend.auditoria.enums.ResultadoAuditoria;
import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.asignaciones.entity.AsignacionCaso;
import com.utp.safezonebackend.asignaciones.repository.AsignacionCasoRepository;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.configuracion.service.ConfiguracionSeguridadService;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.shared.util.DistritoNormalizer;
import com.utp.safezonebackend.usuarios.dto.request.CrearUsuarioRequest;
import com.utp.safezonebackend.usuarios.dto.request.ActualizarUsuarioRequest;
import com.utp.safezonebackend.usuarios.dto.request.CambiarContrasenaRequest;
import com.utp.safezonebackend.usuarios.dto.response.UsuarioResponse;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.mapper.UsuarioMapper;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;
    private final ConfiguracionSeguridadService configuracionSeguridadService;
    private final AsignacionCasoRepository asignacionCasoRepository;
    private final CasoRepository casoRepository;

    public UsuarioService(
            UsuarioRepository repository,
            UsuarioMapper mapper,
            PasswordEncoder passwordEncoder,
            AuditoriaService auditoriaService,
            ConfiguracionSeguridadService configuracionSeguridadService,
            AsignacionCasoRepository asignacionCasoRepository,
            CasoRepository casoRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
        this.configuracionSeguridadService = configuracionSeguridadService;
        this.asignacionCasoRepository = asignacionCasoRepository;
        this.casoRepository = casoRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAll() {
        Usuario actor = obtenerUsuarioAutenticado();
        if (actor.getRol() == RolUsuario.ADMIN || actor.getRol() == RolUsuario.RECEPCIONISTA) {
            return repository.findAll().stream().map(mapper::toResponse).toList();
        }
        if (actor.getRol() == RolUsuario.PSICOLOGO || actor.getRol() == RolUsuario.DEFENSOR) {
            return usuariosVisiblesParaProfesional(actor).stream().map(mapper::toResponse).toList();
        }
        return List.of(mapper.toResponse(actor));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(String id) {
        return mapper.toResponse(obtenerUsuario(id));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPerfilAutenticado() {
        return mapper.toResponse(obtenerUsuarioAutenticado());
    }

    @Transactional
    public UsuarioResponse create(CrearUsuarioRequest request) {
        if (repository.existsByCorreoIgnoreCase(request.correo())) {
            throw new ExcepcionNegocio("El correo ya se encuentra registrado");
        }
        if (repository.existsByDni(request.dni())) {
            throw new ExcepcionNegocio("El DNI ya se encuentra registrado");
        }
        configuracionSeguridadService.validarContrasenaSegura(request.contrasena());

        Usuario actor = obtenerActorActual();
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID().toString());
        usuario.setCorreo(request.correo().trim().toLowerCase());
        usuario.setContrasenaHash(passwordEncoder.encode(request.contrasena()));
        usuario.setNombres(request.nombres().trim());
        usuario.setApellidos(request.apellidos().trim());
        usuario.setDni(request.dni().trim());
        usuario.setTelefono(normalizar(request.telefono()));
        usuario.setDistrito(DistritoNormalizer.normalizar(request.distrito()));
        usuario.setRol(request.rol());
        usuario.setActivo(true);
        usuario.setCreadoPor(actor == null ? null : actor.getId());
        usuario.setFechaCreacion(OffsetDateTime.now());
        usuario.setFechaActualizacion(OffsetDateTime.now());

        Usuario guardado = repository.save(usuario);
        auditarCambio("CREACION_USUARIO", guardado.getId(), actor, null, resumenUsuario(guardado));
        return mapper.toResponse(guardado);
    }

    @Transactional
    public UsuarioResponse update(String id, ActualizarUsuarioRequest request) {
        Usuario usuario = obtenerUsuario(id);
        Usuario actor = obtenerActorActual();
        Map<String, Object> antes = resumenUsuario(usuario);
        RolUsuario rolAnterior = usuario.getRol();
        boolean activoAnterior = usuario.isActivo();

        if (request.correo() != null && repository.existsByCorreoIgnoreCaseAndIdNot(request.correo(), id)) {
            throw new ExcepcionNegocio("El correo ya se encuentra registrado");
        }
        if (request.dni() != null && repository.existsByDniAndIdNot(request.dni(), id)) {
            throw new ExcepcionNegocio("El DNI ya se encuentra registrado");
        }

        if (request.correo() != null) {
            usuario.setCorreo(request.correo().trim().toLowerCase());
        }
        if (request.nombres() != null) {
            usuario.setNombres(request.nombres().trim());
        }
        if (request.apellidos() != null) {
            usuario.setApellidos(request.apellidos().trim());
        }
        if (request.dni() != null) {
            usuario.setDni(request.dni().trim());
        }
        if (request.telefono() != null) {
            usuario.setTelefono(normalizar(request.telefono()));
        }
        if (request.distrito() != null) {
            usuario.setDistrito(DistritoNormalizer.normalizar(request.distrito()));
        }
        if (request.rol() != null) {
            usuario.setRol(request.rol());
        }
        if (request.activo() != null) {
            aplicarCambioActivo(usuario, request.activo(), actor);
        }
        usuario.setActualizadoPor(actor == null ? null : actor.getId());
        usuario.setFechaActualizacion(OffsetDateTime.now());

        Usuario guardado = repository.save(usuario);
        auditarCambio(resolverAccionAuditoria(request, rolAnterior, activoAnterior, guardado), id, actor, antes, resumenUsuario(guardado));
        return mapper.toResponse(guardado);
    }

    @Transactional
    public UsuarioResponse cambiarContrasenaAutenticado(CambiarContrasenaRequest request) {
        Usuario usuario = obtenerUsuarioAutenticado();
        if (!passwordEncoder.matches(request.contrasenaActual(), usuario.getContrasenaHash())) {
            throw new ExcepcionNegocio("La contrasena actual no es correcta");
        }
        if (!request.nuevaContrasena().equals(request.confirmarContrasena())) {
            throw new ExcepcionNegocio("La confirmacion de contrasena no coincide");
        }
        if (passwordEncoder.matches(request.nuevaContrasena(), usuario.getContrasenaHash())) {
            throw new ExcepcionNegocio("La nueva contrasena debe ser diferente a la actual");
        }
        configuracionSeguridadService.validarContrasenaSegura(request.nuevaContrasena());

        usuario.setContrasenaHash(passwordEncoder.encode(request.nuevaContrasena()));
        usuario.setActualizadoPor(usuario.getId());
        usuario.setFechaActualizacion(OffsetDateTime.now());
        Usuario guardado = repository.save(usuario);
        auditarCambio("CAMBIO_CONTRASENA_PROPIA", guardado.getId(), guardado, null, resumenUsuario(guardado));
        return mapper.toResponse(guardado);
    }

    @Transactional
    public void inactivar(String id) {
        Usuario usuario = obtenerUsuario(id);
        Usuario actor = obtenerActorActual();
        Map<String, Object> antes = resumenUsuario(usuario);
        aplicarCambioActivo(usuario, false, actor);
        usuario.setActualizadoPor(actor == null ? null : actor.getId());
        usuario.setFechaActualizacion(OffsetDateTime.now());
        repository.save(usuario);
        auditarCambio("INACTIVACION_USUARIO", id, actor, antes, resumenUsuario(usuario));
    }

    private void aplicarCambioActivo(Usuario usuario, boolean activo, Usuario actor) {
        if (!activo && usuario.getRol() == RolUsuario.ADMIN && repository.countByRolAndActivoTrue(RolUsuario.ADMIN) <= 1) {
            throw new ExcepcionNegocio("No se puede inactivar al unico administrador activo");
        }
        usuario.setActivo(activo);
        if (activo) {
            usuario.setInactivadoPor(null);
            usuario.setFechaInactivacion(null);
        } else {
            usuario.setInactivadoPor(actor == null ? null : actor.getId());
            usuario.setFechaInactivacion(OffsetDateTime.now());
        }
    }

    private Usuario obtenerUsuario(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }

    private Usuario obtenerActorActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return repository.buscarPorCorreo(auth.getName()).orElse(null);
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            throw new ExcepcionNegocio("Usuario no autenticado");
        }
        return repository.buscarPorCorreo(auth.getName())
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new ExcepcionNegocio("Usuario no autenticado"));
    }

    private List<Usuario> usuariosVisiblesParaProfesional(Usuario actor) {
        Set<String> idsVisibles = new HashSet<>();
        idsVisibles.add(actor.getId());

        List<String> casoIds = asignacionCasoRepository
                .findByProfesionalIdAndActivoTrueOrderByFechaAsignacionDesc(actor.getId())
                .stream()
                .map(AsignacionCaso::getCasoId)
                .distinct()
                .toList();
        if (!casoIds.isEmpty()) {
            casoRepository.findByIdInAndActivoTrueOrderByFechaCreacionDesc(casoIds)
                    .forEach(caso -> idsVisibles.add(caso.getVictimaId()));
        }

        List<Usuario> usuariosVisibles = new ArrayList<>();
        usuariosVisibles.addAll(repository.findByRolInAndActivoTrueOrderByNombresAscApellidosAsc(
                List.of(RolUsuario.PSICOLOGO, RolUsuario.DEFENSOR)
        ));
        usuariosVisibles.addAll(repository.findByIdIn(idsVisibles));

        Map<String, Usuario> unicos = new LinkedHashMap<>();
        usuariosVisibles.forEach(usuario -> unicos.put(usuario.getId(), usuario));
        return new ArrayList<>(unicos.values());
    }

    private void auditarCambio(String accion, String entidadId, Usuario actor, Map<String, Object> antes, Map<String, Object> despues) {
        auditoriaService.registrarAccion(new RegistroAuditoriaInterna(
                "USUARIOS",
                actor == null ? null : actor.getId(),
                actor == null ? null : actor.getRol(),
                accion,
                entidadId,
                ResultadoAuditoria.OK,
                "Gestion de usuarios y roles",
                antes,
                despues,
                null,
                null,
                UUID.randomUUID().toString()
        ));
    }

    private Map<String, Object> resumenUsuario(Usuario usuario) {
        return Map.of(
                "id", usuario.getId(),
                "correo", usuario.getCorreo(),
                "rol", usuario.getRol().name(),
                "activo", usuario.isActivo()
        );
    }

    private String resolverAccionAuditoria(ActualizarUsuarioRequest request, RolUsuario rolAnterior, boolean activoAnterior, Usuario usuario) {
        if (request.rol() != null && rolAnterior != usuario.getRol()) {
            return "CAMBIO_ROL_USUARIO";
        }
        if (request.activo() != null && activoAnterior != usuario.isActivo()) {
            return usuario.isActivo() ? "ACTIVACION_USUARIO" : "INACTIVACION_USUARIO";
        }
        return "MODIFICACION_USUARIO";
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
