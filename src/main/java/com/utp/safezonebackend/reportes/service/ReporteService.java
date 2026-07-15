package com.utp.safezonebackend.reportes.service;

import com.utp.safezonebackend.reportes.dto.request.ReporteMensualRequest;
import com.utp.safezonebackend.reportes.dto.response.ReporteMensualResponse;
import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.citas.entity.Cita;
import com.utp.safezonebackend.citas.enums.EstadoCita;
import com.utp.safezonebackend.citas.repository.CitaRepository;
import com.utp.safezonebackend.denuncias.entity.Denuncia;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteService {

    private final DenunciaRepository denunciaRepository;
    private final CasoRepository casoRepository;
    private final CitaRepository citaRepository;

    public ReporteService(
            DenunciaRepository denunciaRepository,
            CasoRepository casoRepository,
            CitaRepository citaRepository
    ) {
        this.denunciaRepository = denunciaRepository;
        this.casoRepository = casoRepository;
        this.citaRepository = citaRepository;
    }

    @Transactional(readOnly = true)
    public ReporteMensualResponse generarMensual(ReporteMensualRequest request) {
        OffsetDateTime desde = request.fechaDesde() == null
                ? OffsetDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay().atOffset(OffsetDateTime.now().getOffset())
                : request.fechaDesde();
        OffsetDateTime hasta = request.fechaHasta() == null ? OffsetDateTime.now() : request.fechaHasta();
        String tipoViolencia = limpiar(request.tipoViolencia());
        NivelRiesgo nivelRiesgo = request.nivelRiesgo();

        List<Denuncia> denuncias = denunciaRepository.findByActivoTrueOrderByFechaCreacionDesc().stream()
                .filter(denuncia -> dentroDeRango(fechaBase(denuncia), desde, hasta))
                .filter(denuncia -> tipoViolencia == null || tipoViolencia.equalsIgnoreCase(denuncia.getTipoViolencia()))
                .filter(denuncia -> nivelRiesgo == null || nivelRiesgo == denuncia.getNivelRiesgo())
                .toList();

        List<String> casoIds = denuncias.stream()
                .map(Denuncia::getCasoId)
                .distinct()
                .toList();
        List<Caso> casos = casoRepository.findByActivoTrueOrderByFechaCreacionDesc().stream()
                .filter(caso -> casoIds.contains(caso.getId()))
                .toList();
        List<Cita> citas = citaRepository.findByActivoTrueOrderByFechaInicioDesc().stream()
                .filter(cita -> dentroDeRango(cita.getFechaInicio(), desde, hasta))
                .toList();

        Map<String, Long> porTipoViolencia = denuncias.stream()
                .collect(Collectors.groupingBy(Denuncia::getTipoViolencia, Collectors.counting()));
        Map<NivelRiesgo, Long> porNivelRiesgo = denuncias.stream()
                .collect(Collectors.groupingBy(Denuncia::getNivelRiesgo, () -> new EnumMap<>(NivelRiesgo.class), Collectors.counting()));
        Map<String, Long> porDistrito = denuncias.stream()
                .collect(Collectors.groupingBy(denuncia -> denuncia.getDistrito() == null ? "Sin distrito" : denuncia.getDistrito(), Collectors.counting()));
        Map<EstadoCaso, Long> casosPorEstado = casos.stream()
                .collect(Collectors.groupingBy(Caso::getEstado, () -> new EnumMap<>(EstadoCaso.class), Collectors.counting()));
        Map<EstadoCita, Long> citasPorEstado = citas.stream()
                .collect(Collectors.groupingBy(Cita::getEstado, () -> new EnumMap<>(EstadoCita.class), Collectors.counting()));

        return new ReporteMensualResponse(
                desde,
                hasta,
                denuncias.size(),
                casos.size(),
                citas.size(),
                citasPorEstado.getOrDefault(EstadoCita.ATENDIDA, 0L),
                citasPorEstado.getOrDefault(EstadoCita.CANCELADA, 0L),
                citasPorEstado.getOrDefault(EstadoCita.NO_ASISTIO, 0L),
                porTipoViolencia,
                porNivelRiesgo,
                porDistrito,
                casosPorEstado,
                citasPorEstado
        );
    }

    private boolean dentroDeRango(OffsetDateTime fecha, OffsetDateTime desde, OffsetDateTime hasta) {
        if (fecha == null) {
            return false;
        }
        return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
    }

    private OffsetDateTime fechaBase(Denuncia denuncia) {
        return denuncia.getFechaIncidente() == null ? denuncia.getFechaCreacion() : denuncia.getFechaIncidente();
    }

    private String limpiar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
