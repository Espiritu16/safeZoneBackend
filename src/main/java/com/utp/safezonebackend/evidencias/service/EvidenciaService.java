package com.utp.safezonebackend.evidencias.service;

import com.utp.safezonebackend.evidencias.dto.request.CrearEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.request.ActualizarEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.request.VincularEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.evidencias.entity.Evidencia;
import com.utp.safezonebackend.evidencias.mapper.EvidenciaMapper;
import com.utp.safezonebackend.evidencias.repository.EvidenciaRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EvidenciaService {

    private final EvidenciaRepository repository;
    private final EvidenciaMapper mapper;
    private final UsuarioRepository usuarioRepository;
    public EvidenciaService(EvidenciaRepository repository, EvidenciaMapper mapper,UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;

    }

    public List<EvidenciaResponse> findAll(String casoId, String denunciaId) {
        List<Evidencia> evidencias;

        if (casoId != null) {
            evidencias = repository.findByCasoId(casoId);
        } else if (denunciaId != null) {
            evidencias = repository.findByDenunciaId(denunciaId);
        } else {
            evidencias = repository.findAll();
        }
        return evidencias.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public EvidenciaResponse findById(String id) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public EvidenciaResponse create(MultipartFile file, String casoId, String denunciaId, String correo) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        var usuario = usuarioRepository.buscarPorCorreo(correo)
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));

        String usuarioId = usuario.getId();

        String nombreUnico = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destino = Paths.get("uploads", nombreUnico);

        try {
            Files.createDirectories(destino.getParent());
            file.transferTo(destino);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo", e);
        }

        Evidencia evidencia = new Evidencia();
        evidencia.setId(UUID.randomUUID().toString());
        evidencia.setCasoId(casoId);
        evidencia.setDenunciaId(denunciaId);
        evidencia.setSubidoPor(usuarioId);
        evidencia.setNombreArchivo(file.getOriginalFilename());
        evidencia.setTipoMime(file.getContentType());
        evidencia.setTamanoBytes(file.getSize());
        evidencia.setUrlAlmacenamiento("/uploads/" + nombreUnico);
        evidencia.setActivo(true);
        evidencia.setFechaCreacion(OffsetDateTime.now());
        evidencia.setCreadoPor(usuarioId);

        Evidencia guardada = repository.save(evidencia);
        return mapper.toResponse(guardada);
    }
    public EvidenciaResponse vincular(String evidenciaId, VincularEvidenciaRequest request, String correo) {
        var evidencia = repository.findById(evidenciaId)
                .orElseThrow(() -> new IllegalArgumentException("Evidencia no encontrada"));

        if (request.casoId() == null && request.denunciaId() == null) {
            throw new IllegalArgumentException("Debe especificar casoId o denunciaId");
        }

        var usuario = usuarioRepository.buscarPorCorreo(correo)
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));

        if (request.casoId() != null) {
            evidencia.setCasoId(request.casoId());
        }
        if (request.denunciaId() != null) {
            evidencia.setDenunciaId(request.denunciaId());
        }

        evidencia.setFechaActualizacion(OffsetDateTime.now());
        evidencia.setActualizadoPor(usuario.getId());

        var actualizada = repository.save(evidencia);
        return mapper.toResponse(actualizada);
    }
    @Transactional
    public void vincularAEvidencias(List<String> evidenciaIds, String casoId, String denunciaId) {
        List<Evidencia> evidencias = repository.findAllById(evidenciaIds);
        evidencias.forEach(e -> {
            e.setCasoId(casoId);
            e.setDenunciaId(denunciaId);
        });
        repository.saveAll(evidencias);
    }
    public EvidenciaResponse update(String id, ActualizarEvidenciaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }
}
