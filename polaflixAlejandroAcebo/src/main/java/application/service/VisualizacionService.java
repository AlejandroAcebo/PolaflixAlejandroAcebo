package application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.entity.seguimientoserie.Visualizacion;
import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Serie;
import application.model.entity.usuario.Usuario;
import application.model.request.VisualizacionRequest;
import application.repository.SerieRepository;
import application.repository.UsuarioRepository;

@Service
public class VisualizacionService {

    private final UsuarioRepository usuarioRepository;
    private final SerieRepository serieRepository;

    public VisualizacionService(
            UsuarioRepository usuarioRepository,
            SerieRepository serieRepository) {
        this.usuarioRepository = usuarioRepository;
        this.serieRepository = serieRepository;
    }

    @Transactional
    public Visualizacion create(int usuarioId, VisualizacionRequest request) {
        Usuario usuario = validarUsuarioExiste(usuarioId);
        Serie serie = validarSerieDelCapitulo(request.getIdCapitulo());
        Capitulo capitulo = serie.buscarCapitulo(request.getIdCapitulo())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el capitulo con id " + request.getIdCapitulo()));

        Visualizacion visualizacion = usuario.registrarVisualizacion(serie, capitulo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La serie no esta en el espacio personal del usuario"));
        usuarioRepository.saveAndFlush(usuario);
        return visualizacion;
    }

    private Usuario validarUsuarioExiste(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + idUsuario));
    }

    private Serie validarSerieDelCapitulo(int idCapitulo) {
        return serieRepository.findByCapituloId(idCapitulo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el capitulo con id " + idCapitulo));
    }
}
