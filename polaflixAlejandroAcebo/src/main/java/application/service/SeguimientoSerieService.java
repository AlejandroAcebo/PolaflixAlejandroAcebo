package application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.entity.serie.Serie;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.usuario.Usuario;
import application.model.request.SeguimientoSerieRequest;
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
    public List<SeguimientoSerie> findByUsuarioId(int usuarioId) {
        validarUsuarioExiste(usuarioId);
        return seguimientoSerieRepository.findByUsuarioIdUsuario(usuarioId);
    }

    @Transactional(readOnly = true)
    public SeguimientoSerie findById(int idSeguimiento) {
        return getSeguimientoById(idSeguimiento);
    }

    @Transactional
    public SeguimientoSerie create(int usuarioId, SeguimientoSerieRequest request) {
        Usuario usuario = validarUsuarioExiste(usuarioId);
        Serie serie = validarSerieExiste(request.getIdSerie());

        return seguimientoSerieRepository.findByUsuarioIdUsuarioAndSerieIdSerie(usuarioId, request.getIdSerie())
                .orElseGet(() -> seguimientoSerieRepository.save(usuario.agregarSeriePendiente(serie)));
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
            throw new ResourceNotFoundException("El usuario no está siguiendo esta serie");
        }
    }

    private SeguimientoSerie getSeguimientoById(int idSeguimiento) {
        return seguimientoSerieRepository.findById(idSeguimiento)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el seguimiento con id " + idSeguimiento));
    }

    private Usuario validarUsuarioExiste(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + idUsuario));
    }

    private Serie validarSerieExiste(int idSerie) {
        return serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la serie con id " + idSerie));
    }
}
