package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.entity.serie.Serie;
import application.model.entity.serie.Temporada;
import application.repository.SerieRepository;
import application.repository.TemporadaRepository;

@Service
public class TemporadaService {
    private final TemporadaRepository temporadaRepository;
    private final SerieRepository serieRepository;

    public TemporadaService(TemporadaRepository temporadaRepository, SerieRepository serieRepository) {
        this.temporadaRepository = temporadaRepository;
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public List<Temporada> findBySerieId(int serieId) {
        validarSerieExiste(serieId);
        return temporadaRepository.findBySerieIdSerie(serieId);
    }

    @Transactional(readOnly = true)
    public Temporada findById(int idTemporada) {
        return getTemporadaById(idTemporada);
    }

    private Temporada getTemporadaById(int idTemporada) {
        return temporadaRepository.findById(idTemporada)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la temporada con id " + idTemporada));
    }

    private Serie validarSerieExiste(int idSerie) {
        return serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la serie con id " + idSerie));
    }
}
