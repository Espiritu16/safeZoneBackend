package com.utp.safezonebackend.victimas.service;

import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.shared.exception.ExcepcionNegocio;
import com.utp.safezonebackend.usuarios.entity.Usuario;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import com.utp.safezonebackend.victimas.dto.response.VictimaHistorialResponse;
import com.utp.safezonebackend.victimas.dto.response.VictimaHistorialResponse.HistorialItem;
import com.utp.safezonebackend.victimas.entity.VictimaAlias;
import com.utp.safezonebackend.victimas.repository.VictimaAliasRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VictimaHistorialService {

    @PersistenceContext
    private EntityManager entityManager;

    private final VictimaAliasRepository victimaAliasRepository;
    private final UsuarioRepository usuarioRepository;

    public VictimaHistorialService(VictimaAliasRepository victimaAliasRepository, UsuarioRepository usuarioRepository) {
        this.victimaAliasRepository = victimaAliasRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public VictimaHistorialResponse obtenerHistorialPorVictima(String victimaId) {
        return obtenerPorVictimaId(victimaId);
    }

    @Transactional(readOnly = true)
    public VictimaHistorialResponse obtenerHistorialAutenticado() {
        Usuario usuario = obtenerUsuarioAutenticado();
        return obtenerPorVictimaId(usuario.getId());
    }

    @Transactional(readOnly = true)
    public VictimaHistorialResponse obtenerPorVictimaId(String victimaId) {
        if (!usuarioRepository.existsById(victimaId)) {
            throw new RecursoNoEncontradoException("Victima no encontrada con ID: " + victimaId);
        }
        String aliasActivo = obtenerAliasActivo(victimaId);
        List<HistorialItem> casos = obtenerCasos(victimaId);
        List<HistorialItem> denuncias = obtenerDenuncias(victimaId);
        List<HistorialItem> citas = obtenerCitas(victimaId);
        List<HistorialItem> seguimientos = obtenerSeguimientos(victimaId);
        List<HistorialItem> evidencias = obtenerEvidencias(victimaId);
        List<HistorialItem> lineaTiempo = new ArrayList<>();
        lineaTiempo.addAll(casos);
        lineaTiempo.addAll(denuncias);
        lineaTiempo.addAll(citas);
        lineaTiempo.addAll(seguimientos);
        lineaTiempo.addAll(evidencias);
        lineaTiempo.sort(Comparator.comparing(HistorialItem::fecha, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        return new VictimaHistorialResponse(
                victimaId,
                aliasActivo,
                casos,
                denuncias,
                citas,
                seguimientos,
                evidencias,
                lineaTiempo
        );
    }

    @Transactional(readOnly = true)
    public VictimaHistorialResponse obtenerPorAlias(String aliasCodigo) {
        VictimaAlias alias = victimaAliasRepository
                .findTopByAliasCodigoIgnoreCaseAndActivoTrueOrderByFechaAsignacionDesc(aliasCodigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alias de victima no encontrado"));
        return obtenerPorVictimaId(alias.getVictima().getId());
    }

    private List<HistorialItem> obtenerCasos(String victimaId) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
                SELECT id, estado, prioridad, resumen, distrito, fecha_creacion, fecha_actualizacion, fecha_cierre
                FROM caso
                WHERE victima_id = :victimaId AND activo = 1
                ORDER BY fecha_actualizacion DESC
                """)
                .setParameter("victimaId", victimaId)
                .getResultList();

        return rows.stream()
                .map(row -> item(
                        "CASO",
                        str(row[0]),
                        str(row[0]),
                        valorO(row[3], "Caso registrado"),
                        "Distrito: " + valorO(row[4], "No especificado"),
                        str(row[1]),
                        fecha(row[6]) != null ? fecha(row[6]) : fecha(row[5]),
                        Map.of(
                                "prioridad", valorO(row[2], "N/A"),
                                "distrito", valorO(row[4], "N/A"),
                                "fechaCierre", valorO(row[7], "N/A")
                        )
                ))
                .toList();
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            throw new ExcepcionNegocio("Usuario no autenticado");
        }
        Usuario usuario = usuarioRepository.buscarPorCorreo(auth.getName())
                .orElseThrow(() -> new ExcepcionNegocio("Usuario no autenticado"));
        if (!usuario.isActivo()) {
            throw new ExcepcionNegocio("El usuario no se encuentra habilitado");
        }
        return usuario;
    }

    private String obtenerAliasActivo(String victimaId) {
        return victimaAliasRepository
                .findTopByVictimaIdAndActivoTrueOrderByFechaAsignacionDesc(victimaId)
                .map(VictimaAlias::getAliasCodigo)
                .orElse(null);
    }

    private List<HistorialItem> obtenerDenuncias(String victimaId) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
                SELECT id, caso_id, tipo_violencia, descripcion, nivel_riesgo, fecha_creacion
                FROM denuncia
                WHERE victima_id = :victimaId AND activo = 1
                ORDER BY fecha_creacion DESC
                """)
                .setParameter("victimaId", victimaId)
                .getResultList();

        return rows.stream()
                .map(row -> item(
                        "DENUNCIA",
                        str(row[0]),
                        str(row[1]),
                        valorO(row[2], "Denuncia registrada"),
                        str(row[3]),
                        str(row[4]),
                        fecha(row[5]),
                        Map.of("tipoViolencia", valorO(row[2], "N/A"), "nivelRiesgo", valorO(row[4], "N/A"))
                ))
                .toList();
    }

    private List<HistorialItem> obtenerCitas(String victimaId) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
                SELECT id, caso_id, tipo_cita, estado, observaciones, fecha_inicio
                FROM cita
                WHERE victima_id = :victimaId AND activo = 1
                ORDER BY fecha_inicio DESC
                """)
                .setParameter("victimaId", victimaId)
                .getResultList();

        return rows.stream()
                .map(row -> item(
                        "CITA",
                        str(row[0]),
                        str(row[1]),
                        "Cita " + valorO(row[2], "programada"),
                        str(row[4]),
                        str(row[3]),
                        fecha(row[5]),
                        Map.of("tipoCita", valorO(row[2], "N/A"))
                ))
                .toList();
    }

    private List<HistorialItem> obtenerSeguimientos(String victimaId) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
                SELECT s.id, s.caso_id, s.tipo_seguimiento, s.contenido, s.proxima_accion, s.fecha_creacion
                FROM seguimiento_caso s
                INNER JOIN caso c ON c.id = s.caso_id
                WHERE c.victima_id = :victimaId AND s.activo = 1 AND c.activo = 1
                ORDER BY s.fecha_creacion DESC
                """)
                .setParameter("victimaId", victimaId)
                .getResultList();

        return rows.stream()
                .map(row -> item(
                        "SEGUIMIENTO",
                        str(row[0]),
                        str(row[1]),
                        valorO(row[2], "Seguimiento registrado"),
                        str(row[3]),
                        str(row[4]),
                        fecha(row[5]),
                        Map.of("proximaAccion", valorO(row[4], "N/A"))
                ))
                .toList();
    }

    private List<HistorialItem> obtenerEvidencias(String victimaId) {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
                SELECT DISTINCT e.id, e.caso_id, e.nombre_archivo, e.tipo_mime, e.fecha_creacion, e.denuncia_id, e.predenuncia_id
                FROM evidencia e
                LEFT JOIN caso c ON c.id = e.caso_id
                LEFT JOIN denuncia d ON d.id = e.denuncia_id
                LEFT JOIN pre_denuncia pd ON pd.id = e.predenuncia_id
                WHERE e.activo = 1
                  AND (c.victima_id = :victimaId OR d.victima_id = :victimaId OR pd.victima_id = :victimaId)
                ORDER BY e.fecha_creacion DESC
                """)
                .setParameter("victimaId", victimaId)
                .getResultList();

        return rows.stream()
                .map(row -> item(
                        "EVIDENCIA",
                        str(row[0]),
                        str(row[1]),
                        valorO(row[2], "Evidencia digital"),
                        "Archivo asociado al expediente",
                        str(row[3]),
                        fecha(row[4]),
                        Map.of(
                                "denunciaId", valorO(row[5], "N/A"),
                                "predenunciaId", valorO(row[6], "N/A"),
                                "tipoMime", valorO(row[3], "N/A")
                        )
                ))
                .toList();
    }

    private HistorialItem item(
            String tipo,
            String id,
            String casoId,
            String titulo,
            String detalle,
            String estado,
            OffsetDateTime fecha,
            Map<String, Object> metadata
    ) {
        return new HistorialItem(tipo, id, casoId, titulo, detalle, estado, fecha, new LinkedHashMap<>(metadata));
    }

    private String str(Object value) {
        return value == null ? null : value.toString();
    }

    private String valorO(Object value, String fallback) {
        String text = str(value);
        return text == null || text.isBlank() ? fallback : text;
    }

    private OffsetDateTime fecha(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof OffsetDateTime offsetDateTime) {
            return offsetDateTime;
        }
        if (value instanceof Timestamp timestamp) {
            return timestamp.toInstant().atOffset(ZoneOffset.UTC);
        }
        if (value instanceof LocalDateTime localDateTime) {
            return localDateTime.atOffset(ZoneOffset.UTC);
        }
        return null;
    }
}
