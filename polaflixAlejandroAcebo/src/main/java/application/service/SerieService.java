package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
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

    private Serie getSerieById(int idSerie) {
        return serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la serie con id " + idSerie));
    }
}
