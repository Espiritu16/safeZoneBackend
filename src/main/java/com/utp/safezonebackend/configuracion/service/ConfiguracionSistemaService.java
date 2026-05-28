package com.utp.safezonebackend.configuracion.service;

import com.utp.safezonebackend.auditoria.dto.request.RegistroAuditoriaInterna;
import com.utp.safezonebackend.auditoria.enums.ResultadoAuditoria;
import com.utp.safezonebackend.auditoria.service.AuditoriaService;
import com.utp.safezonebackend.configuracion.dto.request.CreateConfiguracionSistemaRequest;
import com.utp.safezonebackend.configuracion.dto.request.UpdateConfiguracionSistemaRequest;
import com.utp.safezonebackend.configuracion.dto.response.ConfiguracionSistemaResponse;
import com.utp.safezonebackend.configuracion.entity.ConfiguracionSistema;
import com.utp.safezonebackend.configuracion.enums.TipoValorConfiguracion;
import com.utp.safezonebackend.configuracion.mapper.ConfiguracionSistemaMapper;
import com.utp.safezonebackend.configuracion.repository.ConfiguracionSistemaRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracionSistemaService {

    private final ConfiguracionSistemaRepository repository;
    private final ConfiguracionSistemaMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;

    public ConfiguracionSistemaService(
            ConfiguracionSistemaRepository repository,
            ConfiguracionSistemaMapper mapper,
            UsuarioRepository usuarioRepository,
            AuditoriaService auditoriaService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional(readOnly = true)
    public List<ConfiguracionSistemaResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ConfiguracionSistemaResponse findById(Long id) {
        return mapper.toResponse(obtenerConfiguracion(id));
    }

    @Transactional
    public ConfiguracionSistemaResponse create(CreateConfiguracionSistemaRequest request) {
        String clave = normalizarClave(request.clave());
        validarValor(request.tipoValor(), request.valor());
        if (repository.existsByClaveIgnoreCase(clave)) {
            throw new ExcepcionNegocio("La clave de configuracion ya existe");
        }

        Usuario actor = obtenerActorActual();
        ConfiguracionSistema configuracion = new ConfiguracionSistema();
        configuracion.setClave(clave);
        configuracion.setValor(request.valor().trim());
        configuracion.setTipoValor(request.tipoValor());
        configuracion.setDescripcion(normalizar(request.descripcion()));
        configuracion.setActivo(true);
        configuracion.setCreadoPor(actor == null ? null : actor.getId());
        configuracion.setFechaCreacion(OffsetDateTime.now());
        configuracion.setFechaActualizacion(OffsetDateTime.now());

        ConfiguracionSistema guardada = repository.save(configuracion);
        auditarCambio("CREACION_CONFIGURACION_SEGURIDAD", guardada.getId(), actor, null, resumenConfiguracion(guardada));
        return mapper.toResponse(guardada);
    }

    @Transactional
    public ConfiguracionSistemaResponse update(Long id, UpdateConfiguracionSistemaRequest request) {
        ConfiguracionSistema configuracion = obtenerConfiguracion(id);
        Usuario actor = obtenerActorActual();
        Map<String, Object> antes = resumenConfiguracion(configuracion);

        TipoValorConfiguracion tipoValor = request.tipoValor() == null ? configuracion.getTipoValor() : request.tipoValor();
        String valor = request.valor() == null ? configuracion.getValor() : request.valor();
        validarValor(tipoValor, valor);

        if (request.valor() != null) {
            configuracion.setValor(request.valor().trim());
        }
        if (request.tipoValor() != null) {
            configuracion.setTipoValor(request.tipoValor());
        }
        if (request.descripcion() != null) {
            configuracion.setDescripcion(normalizar(request.descripcion()));
        }
        if (request.activo() != null) {
            aplicarActivo(configuracion, request.activo(), actor);
        }
        configuracion.setActualizadoPor(actor == null ? null : actor.getId());
        configuracion.setFechaActualizacion(OffsetDateTime.now());

        ConfiguracionSistema guardada = repository.save(configuracion);
        auditarCambio("MODIFICACION_CONFIGURACION_SEGURIDAD", id, actor, antes, resumenConfiguracion(guardada));
        return mapper.toResponse(guardada);
    }

    @Transactional
    public void inactivar(Long id) {
        ConfiguracionSistema configuracion = obtenerConfiguracion(id);
        Usuario actor = obtenerActorActual();
        Map<String, Object> antes = resumenConfiguracion(configuracion);
        aplicarActivo(configuracion, false, actor);
        configuracion.setActualizadoPor(actor == null ? null : actor.getId());
        configuracion.setFechaActualizacion(OffsetDateTime.now());
        repository.save(configuracion);
        auditarCambio("INACTIVACION_CONFIGURACION_SEGURIDAD", id, actor, antes, resumenConfiguracion(configuracion));
    }

    private ConfiguracionSistema obtenerConfiguracion(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Configuracion no encontrada"));
    }

    private void validarValor(TipoValorConfiguracion tipoValor, String valor) {
        if (valor == null || valor.isBlank()) {
            throw new ExcepcionNegocio("El valor de configuracion es obligatorio");
        }
        if (tipoValor == TipoValorConfiguracion.NUMBER) {
            try {
                Long.parseLong(valor.trim());
            } catch (NumberFormatException ex) {
                throw new ExcepcionNegocio("El valor debe ser numerico");
            }
        }
        if (tipoValor == TipoValorConfiguracion.BOOLEAN
                && !valor.trim().equalsIgnoreCase("true")
                && !valor.trim().equalsIgnoreCase("false")) {
            throw new ExcepcionNegocio("El valor debe ser true o false");
        }
    }

    private void aplicarActivo(ConfiguracionSistema configuracion, boolean activo, Usuario actor) {
        configuracion.setActivo(activo);
        if (activo) {
            configuracion.setInactivadoPor(null);
            configuracion.setFechaInactivacion(null);
        } else {
            configuracion.setInactivadoPor(actor == null ? null : actor.getId());
            configuracion.setFechaInactivacion(OffsetDateTime.now());
        }
    }

    private Usuario obtenerActorActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            return null;
        }
        return usuarioRepository.buscarPorCorreo(auth.getName()).orElse(null);
    }

    private void auditarCambio(String accion, Long entidadId, Usuario actor, Map<String, Object> antes, Map<String, Object> despues) {
        auditoriaService.registrarAccion(new RegistroAuditoriaInterna(
                "CONFIGURACION_SISTEMA",
                actor == null ? null : actor.getId(),
                actor == null ? null : actor.getRol(),
                accion,
                String.valueOf(entidadId),
                ResultadoAuditoria.OK,
                "Gestion de configuracion de seguridad",
                antes,
                despues,
                null,
                null,
                UUID.randomUUID().toString()
        ));
    }

    private Map<String, Object> resumenConfiguracion(ConfiguracionSistema configuracion) {
        Map<String, Object> resumen = new LinkedHashMap<>();
        resumen.put("id", configuracion.getId());
        resumen.put("clave", configuracion.getClave());
        resumen.put("valor", configuracion.getValor());
        resumen.put("tipoValor", configuracion.getTipoValor().name());
        resumen.put("activo", configuracion.isActivo());
        return resumen;
    }

    private String normalizarClave(String valor) {
        return valor.trim().toUpperCase();
    }

    private String normalizar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
