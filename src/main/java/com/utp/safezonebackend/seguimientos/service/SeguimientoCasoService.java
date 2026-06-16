package com.utp.safezonebackend.seguimientos.service;

import com.utp.safezonebackend.seguimientos.dto.request.CrearSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.dto.request.ActualizarSeguimientoCasoRequest;
import com.utp.safezonebackend.seguimientos.dto.response.SeguimientoCasoResponse;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.seguimientos.entity.SeguimientoCaso;
import com.utp.safezonebackend.seguimientos.mapper.SeguimientoCasoMapper;
import com.utp.safezonebackend.seguimientos.repository.SeguimientoCasoRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeguimientoCasoService {

    private final SeguimientoCasoRepository repository;
    private final SeguimientoCasoMapper mapper;
    private final CasoRepository casoRepository;
    private final UsuarioRepository usuarioRepository;

    public SeguimientoCasoService(
            SeguimientoCasoRepository repository,
            SeguimientoCasoMapper mapper,
            CasoRepository casoRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.casoRepository = casoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<SeguimientoCasoResponse> findAll() {
        return repository.findByActivoTrueOrderByFechaCreacionDesc().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SeguimientoCasoResponse> buscar(String casoId, String autorId) {
        List<SeguimientoCaso> seguimientos = repository.findByActivoTrueOrderByFechaCreacionDesc();
        if (casoId != null && !casoId.isBlank()) {
            seguimientos = seguimientos.stream().filter(s -> casoId.equals(s.getCasoId())).toList();
        }
        if (autorId != null && !autorId.isBlank()) {
            seguimientos = seguimientos.stream().filter(s -> autorId.equals(s.getAutorId())).toList();
        }
        return seguimientos.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SeguimientoCasoResponse findById(String id) {
        return mapper.toResponse(obtenerActivo(id));
    }

    @Transactional
    public SeguimientoCasoResponse create(CrearSeguimientoCasoRequest request) {
        validarCasoActivo(request.casoId());
        Usuario autor = validarAutor(request.autorId());
        OffsetDateTime ahora = OffsetDateTime.now();
        SeguimientoCaso seguimiento = new SeguimientoCaso();
        seguimiento.setId(UUID.randomUUID().toString());
        seguimiento.setCasoId(request.casoId().trim());
        seguimiento.setAutorId(autor.getId());
        seguimiento.setRolAutor(autor.getRol());
        seguimiento.setTipoSeguimiento(limpiar(request.tipoSeguimiento()));
        seguimiento.setContenido(limpiar(request.contenido()));
        seguimiento.setProximaAccion(limpiar(request.proximaAccion()));
        seguimiento.setFechaProximaAccion(request.fechaProximaAccion());
        seguimiento.setActivo(true);
        seguimiento.setFechaCreacion(ahora);
        seguimiento.setFechaActualizacion(ahora);
        return mapper.toResponse(repository.save(seguimiento));
    }

    @Transactional
    public SeguimientoCasoResponse update(String id, ActualizarSeguimientoCasoRequest request) {
        SeguimientoCaso seguimiento = obtenerActivo(id);
        if (request.tipoSeguimiento() != null) {
            seguimiento.setTipoSeguimiento(limpiar(request.tipoSeguimiento()));
        }
        if (request.contenido() != null) {
            seguimiento.setContenido(limpiar(request.contenido()));
        }
        if (request.proximaAccion() != null) {
            seguimiento.setProximaAccion(limpiar(request.proximaAccion()));
        }
        if (request.fechaProximaAccion() != null) {
            seguimiento.setFechaProximaAccion(request.fechaProximaAccion());
        }
        seguimiento.setFechaActualizacion(OffsetDateTime.now());
        return mapper.toResponse(repository.save(seguimiento));
    }

    @Transactional
    public void inactivar(String id) {
        SeguimientoCaso seguimiento = obtenerActivo(id);
        seguimiento.setActivo(false);
        seguimiento.setFechaInactivacion(OffsetDateTime.now());
        seguimiento.setFechaActualizacion(OffsetDateTime.now());
        repository.save(seguimiento);
    }

    private SeguimientoCaso obtenerActivo(String id) {
        return repository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Seguimiento no encontrado"));
    }

    private void validarCasoActivo(String casoId) {
        Caso caso = casoRepository.findByIdAndActivoTrue(casoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Caso no encontrado"));
        if (!caso.isActivo()) {
            throw new RecursoNoEncontradoException("Caso no encontrado");
        }
    }

    private Usuario validarAutor(String autorId) {
        Usuario autor = usuarioRepository.findById(autorId)
                .filter(Usuario::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Autor no encontrado"));
        if (autor.getRol() != RolUsuario.PSICOLOGO && autor.getRol() != RolUsuario.DEFENSOR && autor.getRol() != RolUsuario.ADMIN) {
            throw new ExcepcionNegocio("Solo profesionales autorizados pueden registrar seguimientos");
        }
        return autor;
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
