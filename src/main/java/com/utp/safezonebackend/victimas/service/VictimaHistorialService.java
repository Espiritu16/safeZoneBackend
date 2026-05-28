package com.utp.safezonebackend.victimas.service;

import com.utp.safezonebackend.casos.entity.Caso;
import com.utp.safezonebackend.casos.repository.CasoRepository;
import com.utp.safezonebackend.citas.mapper.CitaMapper;
import com.utp.safezonebackend.citas.repository.CitaRepository;
import com.utp.safezonebackend.denuncias.mapper.DenunciaMapper;
import com.utp.safezonebackend.denuncias.repository.DenunciaRepository;
import com.utp.safezonebackend.evidencias.mapper.EvidenciaMapper;
import com.utp.safezonebackend.evidencias.repository.EvidenciaRepository;
import com.utp.safezonebackend.seguimientos.mapper.SeguimientoCasoMapper;
import com.utp.safezonebackend.seguimientos.repository.SeguimientoCasoRepository;
import com.utp.safezonebackend.victimas.dto.response.VictimaHistorialResponse;
import com.utp.safezonebackend.shared.exception.RecursoNoEncontradoException;
import com.utp.safezonebackend.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VictimaHistorialService {

    private final DenunciaRepository denunciaRepository;
    private final DenunciaMapper denunciaMapper;

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;

    private final CasoRepository casoRepository;

    private final SeguimientoCasoRepository seguimientoCasoRepository;
    private final SeguimientoCasoMapper seguimientoCasoMapper;

    private final EvidenciaRepository evidenciaRepository;
    private final EvidenciaMapper evidenciaMapper;
    
    private final UsuarioRepository usuarioRepository;

    public VictimaHistorialService(
            DenunciaRepository denunciaRepository, DenunciaMapper denunciaMapper,
            CitaRepository citaRepository, CitaMapper citaMapper,
            CasoRepository casoRepository,
            SeguimientoCasoRepository seguimientoCasoRepository, SeguimientoCasoMapper seguimientoCasoMapper,
            EvidenciaRepository evidenciaRepository, EvidenciaMapper evidenciaMapper,
            UsuarioRepository usuarioRepository) {
        this.denunciaRepository = denunciaRepository;
        this.denunciaMapper = denunciaMapper;
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
        this.casoRepository = casoRepository;
        this.seguimientoCasoRepository = seguimientoCasoRepository;
        this.seguimientoCasoMapper = seguimientoCasoMapper;
        this.evidenciaRepository = evidenciaRepository;
        this.evidenciaMapper = evidenciaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public VictimaHistorialResponse obtenerHistorialPorVictima(String victimaId) {
        if (!usuarioRepository.existsById(victimaId)) {
            throw new RecursoNoEncontradoException("Víctima no encontrada con ID: " + victimaId);
        }

        VictimaHistorialResponse response = new VictimaHistorialResponse();
        response.setVictimaId(victimaId);

        // Denuncias
        var denuncias = denunciaRepository.findByVictimaIdAndEliminadoFalse(victimaId);
        response.setDenuncias(denuncias.stream().map(denunciaMapper::toResponse).collect(Collectors.toList()));

        // Citas
        var citas = citaRepository.findByVictimaIdAndEliminadoFalse(victimaId);
        response.setCitas(citas.stream().map(citaMapper::toResponse).collect(Collectors.toList()));

        // Para Seguimientos y Evidencias, primero obtenemos los IDs de los casos de esta víctima
        List<String> casoIds = casoRepository.findByVictimaIdAndEliminadoFalse(victimaId)
                .stream().map(Caso::getId).collect(Collectors.toList());

        // También necesitamos los IDs de las denuncias para las evidencias
        List<String> denunciaIds = denuncias.stream().map(d -> d.getId()).collect(Collectors.toList());

        if (!casoIds.isEmpty()) {
            // Seguimientos
            var seguimientos = seguimientoCasoRepository.findByCasoIdInAndEliminadoFalse(casoIds);
            response.setSeguimientos(seguimientos.stream().map(seguimientoCasoMapper::toResponse).collect(Collectors.toList()));

            // Evidencias (por casoId)
            var evidenciasPorCaso = evidenciaRepository.findByCasoIdInAndEliminadoFalse(casoIds);
            var evidencias = evidenciasPorCaso.stream().collect(Collectors.toList());
            
            // Evidencias (por denunciaId) - si las denuncias no están vacías
            if (!denunciaIds.isEmpty()) {
                 var evidenciasPorDenuncia = evidenciaRepository.findByDenunciaIdInAndEliminadoFalse(denunciaIds);
                 for(var ev : evidenciasPorDenuncia) {
                     if(evidencias.stream().noneMatch(e -> e.getId().equals(ev.getId()))) {
                         evidencias.add(ev);
                     }
                 }
            }
            
            response.setEvidencias(evidencias.stream().map(evidenciaMapper::toResponse).collect(Collectors.toList()));
        } else {
            response.setSeguimientos(List.of());
            
            // Aún podríamos tener evidencias por denunciaId
            if (!denunciaIds.isEmpty()) {
                 var evidenciasPorDenuncia = evidenciaRepository.findByDenunciaIdInAndEliminadoFalse(denunciaIds);
                 response.setEvidencias(evidenciasPorDenuncia.stream().map(evidenciaMapper::toResponse).collect(Collectors.toList()));
            } else {
                 response.setEvidencias(List.of());
            }
        }

        return response;
    }
}
