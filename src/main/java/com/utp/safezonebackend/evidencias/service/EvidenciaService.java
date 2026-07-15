package com.utp.safezonebackend.evidencias.service;

import com.utp.safezonebackend.evidencias.dto.request.CrearEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.request.ActualizarEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.request.VincularEvidenciaRequest;
import com.utp.safezonebackend.evidencias.dto.response.EvidenciaResponse;
import com.utp.safezonebackend.evidencias.entity.Evidencia;
import com.utp.safezonebackend.evidencias.mapper.EvidenciaMapper;
import com.utp.safezonebackend.evidencias.repository.EvidenciaRepository;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import com.utp.safezonebackend.predenuncias.repository.PreDenunciaRepository;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.enums.RolUsuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EvidenciaService {

    private static final long MAX_FILE_SIZE_BYTES = 25L * 1024L * 1024L;
    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of(
            "jpg", "jpeg", "png", "webp", "pdf", "mp3", "wav", "m4a", "mp4", "mov", "doc", "docx"
    );
    private static final Set<String> MIME_PERMITIDOS = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "application/pdf",
            "audio/mpeg",
            "audio/wav",
            "audio/x-wav",
            "audio/mp4",
            "audio/x-m4a",
            "video/mp4",
            "video/quicktime",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private final EvidenciaRepository repository;
    private final EvidenciaMapper mapper;
    private final UsuarioRepository usuarioRepository;
    private final PreDenunciaRepository preDenunciaRepository;
    private final CasoRepository casoRepository;
    private final DenunciaRepository denunciaRepository;

    public EvidenciaService(
            EvidenciaRepository repository,
            EvidenciaMapper mapper,
            UsuarioRepository usuarioRepository,
            PreDenunciaRepository preDenunciaRepository,
            CasoRepository casoRepository,
            DenunciaRepository denunciaRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.preDenunciaRepository = preDenunciaRepository;
        this.casoRepository = casoRepository;
        this.denunciaRepository = denunciaRepository;

    }

    public List<EvidenciaResponse> findAll(String casoId, String denunciaId, String predenunciaId) {
        List<Evidencia> evidencias;

        if (casoId != null) {
            evidencias = repository.findByCasoId(casoId);
        } else if (denunciaId != null) {
            evidencias = repository.findByDenunciaId(denunciaId);
        } else if (predenunciaId != null) {
            evidencias = repository.findByPredenunciaId(predenunciaId);
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

    public ArchivoEvidencia obtenerArchivo(String id) {
        return obtenerArchivo(id, null);
    }

    public ArchivoEvidencia obtenerArchivo(String id, String correo) {
        Evidencia evidencia = repository.findById(id)
                .filter(Evidencia::isActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evidencia no encontrada"));
        validarAccesoArchivo(evidencia, correo);
        String nombreAlmacenado = nombreArchivoAlmacenado(evidencia.getUrlAlmacenamiento());
        Path baseUploads = Paths.get("uploads").toAbsolutePath().normalize();
        Path archivo = baseUploads.resolve(nombreAlmacenado).normalize();
        if (!archivo.startsWith(baseUploads) || !Files.exists(archivo) || !Files.isRegularFile(archivo)) {
            throw new RecursoNoEncontradoException("Archivo de evidencia no encontrado");
        }
        return new ArchivoEvidencia(
                new FileSystemResource(archivo),
                evidencia.getNombreArchivo(),
                resolverContentType(archivo, evidencia.getTipoMime())
        );
    }

    public EvidenciaResponse create(MultipartFile file, String casoId, String denunciaId, String predenunciaId, String correo) {
        validarArchivo(file);
        validarContextoCarga(casoId, denunciaId, predenunciaId, correo);

        String usuarioId = null;
        if (!esBlanco(correo)) {
            var usuario = usuarioRepository.buscarPorCorreo(correo)
                    .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));
            usuarioId = usuario.getId();
        }

        String nombreOriginal = nombreSeguro(file.getOriginalFilename());
        String nombreUnico = UUID.randomUUID() + "_" + nombreOriginal;
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
        evidencia.setPredenunciaId(predenunciaId);
        evidencia.setSubidoPor(usuarioId);
        evidencia.setNombreArchivo(nombreOriginal);
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

        if (request.casoId() == null && request.denunciaId() == null && request.predenunciaId() == null) {
            throw new IllegalArgumentException("Debe especificar casoId, denunciaId o predenunciaId");
        }

        var usuario = usuarioRepository.buscarPorCorreo(correo)
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));

        if (request.casoId() != null) {
            evidencia.setCasoId(request.casoId());
        }
        if (request.denunciaId() != null) {
            evidencia.setDenunciaId(request.denunciaId());
        }
        if (request.predenunciaId() != null) {
            validarPredenunciaActiva(request.predenunciaId());
            evidencia.setPredenunciaId(request.predenunciaId());
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
    @Transactional
    public void vincularPredenunciaAFormalizacion(String predenunciaId, String casoId, String denunciaId) {
        List<Evidencia> evidencias = repository.findByPredenunciaId(predenunciaId);
        evidencias.forEach(e -> {
            e.setCasoId(casoId);
            e.setDenunciaId(denunciaId);
            e.setFechaActualizacion(OffsetDateTime.now());
        });
        repository.saveAll(evidencias);
    }
    public EvidenciaResponse update(String id, ActualizarEvidenciaRequest request) {
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    public void inactivar(String id) {
        throw new UnsupportedOperationException("No se permite eliminacion fisica. Use inactivacion por estado/activo.");
    }

    private void validarArchivo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ExcepcionNegocio("El archivo esta vacio");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new ExcepcionNegocio("El archivo supera el limite de 25 MB");
        }
        String nombre = nombreSeguro(file.getOriginalFilename());
        String extension = extension(nombre);
        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new ExcepcionNegocio("Formato de evidencia no permitido");
        }
        String mime = file.getContentType();
        if (mime != null && !mime.isBlank() && !MIME_PERMITIDOS.contains(mime.toLowerCase(Locale.ROOT))) {
            throw new ExcepcionNegocio("Tipo de archivo no permitido");
        }
    }

    private void validarContextoCarga(String casoId, String denunciaId, String predenunciaId, String correo) {
        if (esBlanco(correo) && esBlanco(predenunciaId)) {
            throw new ExcepcionNegocio("Debe iniciar sesion o asociar la evidencia a una predenuncia");
        }
        if (!esBlanco(predenunciaId)) {
            validarPredenunciaActiva(predenunciaId);
        }
    }

    private void validarPredenunciaActiva(String predenunciaId) {
        preDenunciaRepository.findById(predenunciaId)
                .filter(preDenuncia -> preDenuncia.isActivo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Predenuncia no encontrada"));
    }

    private String nombreSeguro(String nombreOriginal) {
        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            throw new ExcepcionNegocio("El archivo debe tener un nombre valido");
        }
        try {
            String nombre = Paths.get(nombreOriginal).getFileName().toString();
            if (nombre.isBlank() || !nombre.contains(".")) {
                throw new ExcepcionNegocio("El archivo debe tener una extension valida");
            }
            return nombre;
        } catch (InvalidPathException ex) {
            throw new ExcepcionNegocio("El archivo debe tener un nombre valido");
        }
    }

    private String extension(String nombreArchivo) {
        int index = nombreArchivo.lastIndexOf('.');
        return index < 0 ? "" : nombreArchivo.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    private String nombreArchivoAlmacenado(String urlAlmacenamiento) {
        if (esBlanco(urlAlmacenamiento)) {
            throw new RecursoNoEncontradoException("Archivo de evidencia no encontrado");
        }
        try {
            String nombre = Paths.get(urlAlmacenamiento).getFileName().toString();
            if (nombre.isBlank()) {
                throw new RecursoNoEncontradoException("Archivo de evidencia no encontrado");
            }
            return nombre;
        } catch (InvalidPathException ex) {
            throw new RecursoNoEncontradoException("Archivo de evidencia no encontrado");
        }
    }

    private String resolverContentType(Path archivo, String tipoMime) {
        if (!esBlanco(tipoMime)) {
            return tipoMime;
        }
        try {
            String detectado = Files.probeContentType(archivo);
            return esBlanco(detectado) ? "application/octet-stream" : detectado;
        } catch (IOException ex) {
            return "application/octet-stream";
        }
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.isBlank();
    }

    private void validarAccesoArchivo(Evidencia evidencia, String correo) {
        if (esBlanco(correo)) {
            return;
        }
        var usuario = usuarioRepository.buscarPorCorreo(correo)
                .filter(u -> u.isActivo())
                .orElseThrow(() -> new ExcepcionNegocio("Usuario no autenticado"));
        if (usuario.getRol() != RolUsuario.VICTIMA) {
            return;
        }
        boolean pertenece = perteneceCaso(evidencia.getCasoId(), usuario.getId())
                || perteneceDenuncia(evidencia.getDenunciaId(), usuario.getId())
                || pertenecePredenuncia(evidencia.getPredenunciaId(), usuario.getId());
        if (!pertenece) {
            throw new ExcepcionNegocio("No tiene permisos para acceder a esta evidencia");
        }
    }

    private boolean perteneceCaso(String casoId, String victimaId) {
        return !esBlanco(casoId)
                && casoRepository.findByIdAndActivoTrue(casoId)
                .map(caso -> victimaId.equals(caso.getVictimaId()))
                .orElse(false);
    }

    private boolean perteneceDenuncia(String denunciaId, String victimaId) {
        return !esBlanco(denunciaId)
                && denunciaRepository.findByIdAndActivoTrue(denunciaId)
                .map(denuncia -> victimaId.equals(denuncia.getVictimaId()))
                .orElse(false);
    }

    private boolean pertenecePredenuncia(String predenunciaId, String victimaId) {
        return !esBlanco(predenunciaId)
                && preDenunciaRepository.findById(predenunciaId)
                .filter(preDenuncia -> preDenuncia.isActivo())
                .map(preDenuncia -> victimaId.equals(preDenuncia.getVictimaId()))
                .orElse(false);
    }

    public record ArchivoEvidencia(Resource resource, String nombreOriginal, String contentType) {
    }
}
