package application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.serie.Serie;
import application.model.entity.usuario.Usuario;
import application.model.request.SeguimientoSerieRequest;
import application.repository.SerieRepository;
import application.repository.UsuarioRepository;

@Service
public class SeguimientoSerieService {

    private final UsuarioRepository usuarioRepository;
    private final SerieRepository serieRepository;

    public SeguimientoSerieService(
            UsuarioRepository usuarioRepository,
            SerieRepository serieRepository) {
        this.usuarioRepository = usuarioRepository;
        this.serieRepository = serieRepository;
    }

    @Transactional
    public SeguimientoSerie create(int usuarioId, SeguimientoSerieRequest request) {
        Usuario usuario = validarUsuarioExiste(usuarioId);
        Serie serie = validarSerieExiste(request.getIdSerie());

        SeguimientoSerie seguimiento = usuario.agregarSeriePendiente(serie);
        usuarioRepository.saveAndFlush(usuario);
        return seguimiento;
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
