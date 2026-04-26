package application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import application.model.dto.visualizacion.VisualizacionRequestDto;
import application.model.dto.visualizacion.VisualizacionResponseDto;
import application.model.entity.serie.Capitulo;
import application.model.entity.seguimientoserie.Visualizacion;
import application.model.entity.usuario.Usuario;
import application.repository.CapituloRepository;
import application.repository.UsuarioRepository;
import application.repository.VisualizacionRepository;

@Service
public class VisualizacionService {
    private final VisualizacionRepository visualizacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CapituloRepository capituloRepository;

    public VisualizacionService(VisualizacionRepository visualizacionRepository, 
                                 UsuarioRepository usuarioRepository, 
                                 CapituloRepository capituloRepository) {
        this.visualizacionRepository = visualizacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.capituloRepository = capituloRepository;
    }

    @Transactional(readOnly = true)
    public List<VisualizacionResponseDto> findByUsuarioId(int usuarioId) {
        validarUsuarioExiste(usuarioId);
        return visualizacionRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VisualizacionResponseDto findById(int idVisualizacion) {
        return toResponse(getVisualizacionById(idVisualizacion));
    }

    @Transactional
    public VisualizacionResponseDto create(int usuarioId, VisualizacionRequestDto request) {
        if (request.getIdCapitulo() <= 0) {
            throw new IllegalArgumentException("El ID del capítulo debe ser positivo");
        }

        Usuario usuario = validarUsuarioExiste(usuarioId);
        Capitulo capitulo = validarCapituloExiste(request.getIdCapitulo());

        Visualizacion visualizacion = Visualizacion.builder()
                .usuario(usuario)
                .capitulo(capitulo)
                .fechaVisualizacion(LocalDate.now())
                .build();

        return toResponse(visualizacionRepository.save(visualizacion));
    }

    @Transactional
    public void delete(int idVisualizacion) {
        Visualizacion visualizacion = getVisualizacionById(idVisualizacion);
        visualizacionRepository.delete(visualizacion);
    }

    private Visualizacion getVisualizacionById(int idVisualizacion) {
        return visualizacionRepository.findById(idVisualizacion)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la visualización con id " + idVisualizacion));
    }

    private Usuario validarUsuarioExiste(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el usuario con id " + idUsuario));
    }

    private Capitulo validarCapituloExiste(int idCapitulo) {
        return capituloRepository.findById(idCapitulo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el capítulo con id " + idCapitulo));
    }

    private VisualizacionResponseDto toResponse(Visualizacion visualizacion) {
        return new VisualizacionResponseDto(
                visualizacion.getIdVisualizacion(),
                visualizacion.getUsuario().getIdUsuario(),
                visualizacion.getCapitulo().getIdCapitulo(),
                visualizacion.getFechaVisualizacion().toString()
        );
    }
}
