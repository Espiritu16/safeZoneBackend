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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
        OffsetDateTime desde = request.fechaDesde();
        OffsetDateTime hasta = request.fechaHasta();
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
        boolean filtrarCitasPorCasos = tipoViolencia != null || nivelRiesgo != null;
        List<Cita> citas = citaRepository.findByActivoTrueOrderByFechaInicioDesc().stream()
                .filter(cita -> dentroDeRango(cita.getFechaInicio(), desde, hasta))
                .filter(cita -> !filtrarCitasPorCasos || casoIds.contains(cita.getCasoId()))
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

    @Transactional(readOnly = true)
    public byte[] generarExcelMensual(ReporteMensualRequest request) {
        ReporteMensualResponse reporte = generarMensual(request);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Sheet resumen = workbook.createSheet("Resumen");
            escribirFila(resumen, 0, headerStyle, "Metrica", "Valor");
            escribirFila(resumen, 1, null, "Desde", reporte.fechaDesde() == null ? "Todos" : reporte.fechaDesde().toString());
            escribirFila(resumen, 2, null, "Hasta", reporte.fechaHasta() == null ? "Todos" : reporte.fechaHasta().toString());
            escribirFila(resumen, 3, null, "Total denuncias", reporte.totalDenuncias());
            escribirFila(resumen, 4, null, "Total casos", reporte.totalCasos());
            escribirFila(resumen, 5, null, "Total citas", reporte.totalCitas());
            escribirFila(resumen, 6, null, "Citas atendidas", reporte.citasAtendidas());
            escribirFila(resumen, 7, null, "Citas canceladas", reporte.citasCanceladas());
            escribirFila(resumen, 8, null, "Citas no asistidas", reporte.citasNoAsistidas());
            autoSize(resumen, 2);

            escribirMapa(workbook, "Tipo violencia", "Tipo", reporte.porTipoViolencia(), headerStyle);
            escribirMapa(workbook, "Nivel riesgo", "Riesgo", reporte.porNivelRiesgo(), headerStyle);
            escribirMapa(workbook, "Distrito", "Distrito", reporte.porDistrito(), headerStyle);
            escribirMapa(workbook, "Casos estado", "Estado", reporte.casosPorEstado(), headerStyle);
            escribirMapa(workbook, "Citas estado", "Estado", reporte.citasPorEstado(), headerStyle);

            workbook.write(output);
            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo generar el reporte Excel", exception);
        }
    }

    private boolean dentroDeRango(OffsetDateTime fecha, OffsetDateTime desde, OffsetDateTime hasta) {
        if (fecha == null) {
            return false;
        }
        return (desde == null || !fecha.isBefore(desde)) && (hasta == null || !fecha.isAfter(hasta));
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

    private void escribirMapa(Workbook workbook, String nombreHoja, String cabecera, Map<?, Long> datos, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet(nombreHoja);
        escribirFila(sheet, 0, headerStyle, cabecera, "Cantidad");
        int rowIndex = 1;
        for (Map.Entry<?, Long> entry : datos.entrySet()) {
            escribirFila(sheet, rowIndex++, null, String.valueOf(entry.getKey()), entry.getValue());
        }
        autoSize(sheet, 2);
    }

    private void escribirFila(Sheet sheet, int rowIndex, CellStyle style, Object... values) {
        Row row = sheet.createRow(rowIndex);
        for (int index = 0; index < values.length; index++) {
            var cell = row.createCell(index);
            if (style != null) {
                cell.setCellStyle(style);
            }
            Object value = values[index];
            if (value instanceof Number number) {
                cell.setCellValue(number.doubleValue());
            } else {
                cell.setCellValue(value == null ? "" : String.valueOf(value));
            }
        }
    }

    private void autoSize(Sheet sheet, int columns) {
        for (int index = 0; index < columns; index++) {
            sheet.autoSizeColumn(index);
        }
    }
}
