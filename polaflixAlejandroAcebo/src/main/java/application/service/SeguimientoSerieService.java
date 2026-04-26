package application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import application.model.dto.seguimientoserie.SeguimientoSerieRequestDto;
import application.model.dto.seguimientoserie.SeguimientoSerieResponseDto;
import application.model.entity.serie.Serie;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.usuario.Usuario;
import application.repository.SerieRepository;
import application.repository.SeguimientoSerieRepository;
import application.repository.UsuarioRepository;

@Service
public class SeguimientoSerieService {
    private final SeguimientoSerieRepository seguimientoSerieRepository;
    private final UsuarioRepository usuarioRepository;
    private final SerieRepository serieRepository;

    public SeguimientoSerieService(SeguimientoSerieRepository seguimientoSerieRepository, 
                                    UsuarioRepository usuarioRepository, 
                                    SerieRepository serieRepository) {
        this.seguimientoSerieRepository = seguimientoSerieRepository;
        this.usuarioRepository = usuarioRepository;
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public List<SeguimientoSerieResponseDto> findByUsuarioId(int usuarioId) {
        validarUsuarioExiste(usuarioId);
        return seguimientoSerieRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SeguimientoSerieResponseDto findById(int idSeguimiento) {
        return toResponse(getSeguimientoById(idSeguimiento));
    }

    @Transactional
    public SeguimientoSerieResponseDto create(int usuarioId, SeguimientoSerieRequestDto request) {
        if (request.getIdSerie() <= 0) {
            throw new IllegalArgumentException("El ID de la serie debe ser positivo");
        }

        Usuario usuario = validarUsuarioExiste(usuarioId);
        Serie serie = validarSerieExiste(request.getIdSerie());

        // Verificar que el usuario no esté ya siguiendo esta serie
        Optional<SeguimientoSerie> yaExiste = seguimientoSerieRepository.findByUsuarioIdUsuarioAndSerieIdSerie(usuarioId, request.getIdSerie());
        if (yaExiste.isPresent()) {
            throw new IllegalArgumentException("El usuario ya está siguiendo esta serie");
        }

        SeguimientoSerie seguimiento = SeguimientoSerie.builder()
                .usuario(usuario)
                .serie(serie)
                .build();

        return toResponse(seguimientoSerieRepository.save(seguimiento));
    }

    @Transactional
    public void delete(int idSeguimiento) {
        SeguimientoSerie seguimiento = getSeguimientoById(idSeguimiento);
        seguimientoSerieRepository.delete(seguimiento);
    }

    @Transactional
    public void deleteBySerie(int usuarioId, int idSerie) {
        Optional<SeguimientoSerie> seguimiento = seguimientoSerieRepository.findByUsuarioIdUsuarioAndSerieIdSerie(usuarioId, idSerie);
        if (seguimiento.isPresent()) {
            seguimientoSerieRepository.delete(seguimiento.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El usuario no está siguiendo esta serie");
        }
    }

    private SeguimientoSerie getSeguimientoById(int idSeguimiento) {
        return seguimientoSerieRepository.findById(idSeguimiento)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el seguimiento con id " + idSeguimiento));
    }

    private Usuario validarUsuarioExiste(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el usuario con id " + idUsuario));
    }

    private Serie validarSerieExiste(int idSerie) {
        return serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la serie con id " + idSerie));
    }

    private SeguimientoSerieResponseDto toResponse(SeguimientoSerie seguimiento) {
        return new SeguimientoSerieResponseDto(
                seguimiento.getIdSeguimientoSerie(),
                seguimiento.getSerie().getIdSerie(),
                seguimiento.getUltimoVisto() != null ? seguimiento.getUltimoVisto().getIdCapitulo() : null,
                seguimiento.getEstadoSerie() != null ? seguimiento.getEstadoSerie().toString() : "SIN_ESTADO"
        );
    }
}
