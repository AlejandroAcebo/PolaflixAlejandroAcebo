package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.dto.serie.SerieRequestDto;
import application.model.entity.serie.Serie;
import application.repository.SerieRepository;

@Service
public class SerieService {
    private final SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public List<Serie> findAll() {
        return serieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Serie findById(int idSerie) {
        return getSerieById(idSerie);
    }

    @Transactional
    public Serie create(SerieRequestDto request) {
        Serie serie = Serie.crear(
                request.getNombreSerie(),
                request.getSinopsis(),
                request.getTipoSerie());

        return serieRepository.save(serie);
    }

    @Transactional
    public Serie update(int idSerie, SerieRequestDto request) {
        Serie serie = getSerieById(idSerie);
        serie.actualizarDatos(request.getNombreSerie(), request.getSinopsis(), request.getTipoSerie());

        return serieRepository.save(serie);
    }

    @Transactional
    public void delete(int idSerie) {
        Serie serie = getSerieById(idSerie);
        serieRepository.delete(serie);
    }

    private Serie getSerieById(int idSerie) {
        return serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la serie con id " + idSerie));
    }
}
