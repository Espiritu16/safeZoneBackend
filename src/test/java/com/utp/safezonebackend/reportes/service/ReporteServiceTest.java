package com.utp.safezonebackend.reportes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.enums.EstadoCaso;
import com.utp.safezonebackend.casos.enums.PrioridadCaso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.citas.entity.Cita;
import com.utp.safezonebackend.citas.enums.EstadoCita;
import com.utp.safezonebackend.citas.repository.CitaRepository;
import com.utp.safezonebackend.denuncias.entity.Denuncia;
import com.utp.safezonebackend.denuncias.enums.NivelRiesgo;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import com.utp.safezonebackend.reportes.dto.request.ReporteMensualRequest;
import com.utp.safezonebackend.reportes.dto.response.ReporteMensualResponse;
import java.io.ByteArrayInputStream;
import java.time.OffsetDateTime;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private DenunciaRepository denunciaRepository;

    @Mock
    private CasoRepository casoRepository;

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private ReporteService service;

    @Test
    void generarMensualFiltraPorFechaTipoViolenciaYRiesgo() {
        OffsetDateTime desde = OffsetDateTime.parse("2026-07-01T00:00:00-05:00");
        OffsetDateTime hasta = OffsetDateTime.parse("2026-07-31T23:59:59-05:00");
        when(denunciaRepository.findByActivoTrueOrderByFechaCreacionDesc()).thenReturn(List.of(
                denuncia("d1", "c1", "FISICA", NivelRiesgo.CRITICO, "Lima", desde.plusDays(1)),
                denuncia("d2", "c2", "PSICOLOGICA", NivelRiesgo.ALTO, "Comas", desde.plusDays(2)),
                denuncia("d3", "c3", "FISICA", NivelRiesgo.BAJO, "Lima", desde.minusDays(1))
        ));
        when(casoRepository.findByActivoTrueOrderByFechaCreacionDesc()).thenReturn(List.of(
                caso("c1", EstadoCaso.EN_ATENCION, PrioridadCaso.CRITICA),
                caso("c2", EstadoCaso.REGISTRADO, PrioridadCaso.ALTA)
        ));
        when(citaRepository.findByActivoTrueOrderByFechaInicioDesc()).thenReturn(List.of(
                cita("c1", EstadoCita.ATENDIDA, desde.plusDays(3)),
                cita("c2", EstadoCita.CANCELADA, desde.plusDays(4))
        ));

        ReporteMensualResponse response = service.generarMensual(new ReporteMensualRequest(
                desde,
                hasta,
                "FISICA",
                NivelRiesgo.CRITICO
        ));

        assertThat(response.totalDenuncias()).isEqualTo(1);
        assertThat(response.porTipoViolencia()).containsEntry("FISICA", 1L);
        assertThat(response.porNivelRiesgo()).containsEntry(NivelRiesgo.CRITICO, 1L);
        assertThat(response.porDistrito()).containsEntry("Comas actualizado", 1L);
        assertThat(response.totalCitas()).isEqualTo(1);
        assertThat(response.citasAtendidas()).isEqualTo(1);
        assertThat(response.citasCanceladas()).isZero();
    }

    @Test
    void generarMensualUsaRiesgoYDistritoActualesDelCaso() {
        OffsetDateTime fecha = OffsetDateTime.parse("2026-07-10T10:00:00-05:00");
        when(denunciaRepository.findByActivoTrueOrderByFechaCreacionDesc()).thenReturn(List.of(
                denuncia("d1", "c1", "FISICA", NivelRiesgo.BAJO, "Distrito denuncia", fecha)
        ));
        when(casoRepository.findByActivoTrueOrderByFechaCreacionDesc()).thenReturn(List.of(
                caso("c1", EstadoCaso.EN_ATENCION, PrioridadCaso.CRITICA, "Distrito editado")
        ));
        when(citaRepository.findByActivoTrueOrderByFechaInicioDesc()).thenReturn(List.of());

        ReporteMensualResponse response = service.generarMensual(new ReporteMensualRequest(
                null,
                null,
                null,
                NivelRiesgo.CRITICO
        ));

        assertThat(response.totalDenuncias()).isEqualTo(1);
        assertThat(response.totalCasos()).isEqualTo(1);
        assertThat(response.porNivelRiesgo()).containsEntry(NivelRiesgo.CRITICO, 1L);
        assertThat(response.porNivelRiesgo()).doesNotContainKey(NivelRiesgo.BAJO);
        assertThat(response.porDistrito()).containsEntry("Distrito editado", 1L);
        assertThat(response.porDistrito()).doesNotContainKey("Distrito denuncia");
    }

    @Test
    void generarMensualSinFechasIncluyeTodoElHistorico() {
        OffsetDateTime fechaAntigua = OffsetDateTime.parse("2025-01-10T10:00:00-05:00");
        OffsetDateTime fechaActual = OffsetDateTime.parse("2026-07-10T10:00:00-05:00");
        when(denunciaRepository.findByActivoTrueOrderByFechaCreacionDesc()).thenReturn(List.of(
                denuncia("d1", "c1", "FISICA", NivelRiesgo.CRITICO, "Lima", fechaAntigua),
                denuncia("d2", "c2", "PSICOLOGICA", NivelRiesgo.ALTO, "Comas", fechaActual)
        ));
        when(casoRepository.findByActivoTrueOrderByFechaCreacionDesc()).thenReturn(List.of(
                caso("c1", EstadoCaso.EN_ATENCION, PrioridadCaso.CRITICA),
                caso("c2", EstadoCaso.REGISTRADO, PrioridadCaso.ALTA)
        ));
        when(citaRepository.findByActivoTrueOrderByFechaInicioDesc()).thenReturn(List.of(
                cita("c1", EstadoCita.ATENDIDA, fechaAntigua),
                cita("c2", EstadoCita.CANCELADA, fechaActual)
        ));

        ReporteMensualResponse response = service.generarMensual(new ReporteMensualRequest(
                null,
                null,
                null,
                null
        ));

        assertThat(response.totalDenuncias()).isEqualTo(2);
        assertThat(response.totalCasos()).isEqualTo(2);
        assertThat(response.totalCitas()).isEqualTo(2);
    }

    @Test
    void generarExcelMensualDevuelveArchivoXlsx() throws Exception {
        OffsetDateTime desde = OffsetDateTime.parse("2026-07-01T00:00:00-05:00");
        OffsetDateTime hasta = OffsetDateTime.parse("2026-07-31T23:59:59-05:00");
        when(denunciaRepository.findByActivoTrueOrderByFechaCreacionDesc()).thenReturn(List.of(
                denuncia("d1", "c1", "FISICA", NivelRiesgo.CRITICO, "Lima", desde.plusDays(1))
        ));
        when(casoRepository.findByActivoTrueOrderByFechaCreacionDesc()).thenReturn(List.of(
                caso("c1", EstadoCaso.EN_ATENCION, PrioridadCaso.CRITICA)
        ));
        when(citaRepository.findByActivoTrueOrderByFechaInicioDesc()).thenReturn(List.of(
                cita("c1", EstadoCita.ATENDIDA, desde.plusDays(3))
        ));

        byte[] archivo = service.generarExcelMensual(new ReporteMensualRequest(
                desde,
                hasta,
                "FISICA",
                NivelRiesgo.CRITICO
        ));

        assertThat(archivo).isNotEmpty();
        assertThat(archivo[0]).isEqualTo((byte) 'P');
        assertThat(archivo[1]).isEqualTo((byte) 'K');
        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(archivo))) {
            assertThat(workbook.getSheet("Resumen").getTables()).hasSize(1);
            assertThat(workbook.getSheet("Resumen").getRow(0).getCell(0).getStringCellValue()).isEqualTo("Metrica");
            assertThat(workbook.getSheet("Resumen").getRow(0).getCell(1).getStringCellValue()).isEqualTo("Valor");
        }
    }

    private Denuncia denuncia(String id, String casoId, String tipo, NivelRiesgo riesgo, String distrito, OffsetDateTime fecha) {
        Denuncia denuncia = new Denuncia();
        denuncia.setId(id);
        denuncia.setCasoId(casoId);
        denuncia.setTipoViolencia(tipo);
        denuncia.setNivelRiesgo(riesgo);
        denuncia.setDistrito(distrito);
        denuncia.setFechaIncidente(fecha);
        denuncia.setFechaCreacion(fecha);
        denuncia.setActivo(true);
        return denuncia;
    }

    private Caso caso(String id, EstadoCaso estado, PrioridadCaso prioridad) {
        return caso(id, estado, prioridad, "Comas actualizado");
    }

    private Caso caso(String id, EstadoCaso estado, PrioridadCaso prioridad, String distrito) {
        Caso caso = new Caso();
        caso.setId(id);
        caso.setEstado(estado);
        caso.setPrioridad(prioridad);
        caso.setDistrito(distrito);
        caso.setActivo(true);
        return caso;
    }

    private Cita cita(String casoId, EstadoCita estado, OffsetDateTime fechaInicio) {
        Cita cita = new Cita();
        cita.setCasoId(casoId);
        cita.setEstado(estado);
        cita.setFechaInicio(fechaInicio);
        cita.setActivo(true);
        return cita;
    }
}
