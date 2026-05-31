package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.entity.serie.Capitulo;
import application.model.entity.seguimientoserie.Visualizacion;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.usuario.Usuario;
import application.model.request.VisualizacionRequest;
import application.repository.CapituloRepository;
import application.repository.SeguimientoSerieRepository;
import application.repository.UsuarioRepository;
import application.repository.VisualizacionRepository;

@Service
public class VisualizacionService {
    private final VisualizacionRepository visualizacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CapituloRepository capituloRepository;
    private final SeguimientoSerieRepository seguimientoSerieRepository;

    public VisualizacionService(VisualizacionRepository visualizacionRepository, 
                                 UsuarioRepository usuarioRepository, 
                                 CapituloRepository capituloRepository,
                                 SeguimientoSerieRepository seguimientoSerieRepository) {
        this.visualizacionRepository = visualizacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.capituloRepository = capituloRepository;
        this.seguimientoSerieRepository = seguimientoSerieRepository;
    }

    @Transactional(readOnly = true)
    public List<Visualizacion> findByUsuarioId(int usuarioId) {
        validarUsuarioExiste(usuarioId);
        return visualizacionRepository.findByUsuarioIdUsuario(usuarioId);
    }

    @Transactional(readOnly = true)
    public Visualizacion findById(int idVisualizacion) {
        return getVisualizacionById(idVisualizacion);
    }

    @Transactional
    public Visualizacion create(int usuarioId, VisualizacionRequest request) {
        Usuario usuario = validarUsuarioExiste(usuarioId);
        Capitulo capitulo = validarCapituloExiste(request.getIdCapitulo());
        int serieId = capitulo.getTemporada().getSerie().getIdSerie();
        SeguimientoSerie seguimiento = seguimientoSerieRepository
                .findByUsuarioIdUsuarioAndSerieIdSerie(usuarioId, serieId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La serie no esta en el espacio personal del usuario"));

        var visualizacionExistente = visualizacionRepository
                .findByUsuarioIdUsuarioAndCapituloIdCapitulo(usuarioId, request.getIdCapitulo());
        if (visualizacionExistente.isPresent()) {
            actualizarSeguimiento(usuarioId, seguimiento, capitulo);
            return visualizacionExistente.get();
        }

        Visualizacion visualizacion = usuario.visualizarCapitulo(capitulo, seguimiento.getSerie());
        Visualizacion saved = visualizacionRepository.save(visualizacion);
        actualizarSeguimiento(usuarioId, seguimiento, capitulo);

        return saved;
    }

    @Transactional
    public void delete(int idVisualizacion) {
        Visualizacion visualizacion = getVisualizacionById(idVisualizacion);
        visualizacionRepository.delete(visualizacion);
    }

    private Visualizacion getVisualizacionById(int idVisualizacion) {
        return visualizacionRepository.findById(idVisualizacion)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la visualización con id " + idVisualizacion));
    }

    private Usuario validarUsuarioExiste(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + idUsuario));
    }

    private Capitulo validarCapituloExiste(int idCapitulo) {
        return capituloRepository.findById(idCapitulo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el capítulo con id " + idCapitulo));
    }

    private void actualizarSeguimiento(
            int usuarioId,
            SeguimientoSerie seguimiento,
            Capitulo capituloFallback) {
        List<Visualizacion> visualizaciones = visualizacionRepository.findByUsuarioIdUsuario(usuarioId);
        seguimiento.registrarVisualizacion(capituloFallback, visualizaciones);
        seguimientoSerieRepository.save(seguimiento);
    }
}
