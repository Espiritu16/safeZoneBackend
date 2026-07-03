package com.utp.safezonebackend.evidencias.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.utp.safezonebackend.evidencias.entity.Evidencia;
import com.utp.safezonebackend.evidencias.mapper.EvidenciaMapper;
import com.utp.safezonebackend.evidencias.repository.EvidenciaRepository;
import com.utp.safezonebackend.predenuncias.repository.PreDenunciaRepository;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvidenciaServiceTest {

    @Mock
    private EvidenciaRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PreDenunciaRepository preDenunciaRepository;

    private final Path uploadsDir = Path.of("uploads");

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(uploadsDir.resolve("evidencia-prueba.txt"));
    }

    @Test
    void obtenerArchivoDevuelveRecursoDesdeRutaAlmacenada() throws Exception {
        Files.createDirectories(uploadsDir);
        Files.writeString(uploadsDir.resolve("evidencia-prueba.txt"), "contenido de prueba");
        Evidencia evidencia = new Evidencia();
        evidencia.setId("evidencia-1");
        evidencia.setNombreArchivo("declaracion.txt");
        evidencia.setTipoMime("text/plain");
        evidencia.setUrlAlmacenamiento("/uploads/evidencia-prueba.txt");
        evidencia.setFechaCreacion(OffsetDateTime.now());
        evidencia.setActivo(true);
        when(repository.findById("evidencia-1")).thenReturn(Optional.of(evidencia));
        EvidenciaService service = new EvidenciaService(
                repository,
                new EvidenciaMapper(),
                usuarioRepository,
                preDenunciaRepository
        );

        EvidenciaService.ArchivoEvidencia archivo = service.obtenerArchivo("evidencia-1");

        assertThat(archivo.resource().exists()).isTrue();
        assertThat(archivo.nombreOriginal()).isEqualTo("declaracion.txt");
        assertThat(archivo.contentType()).isEqualTo("text/plain");
    }
}
