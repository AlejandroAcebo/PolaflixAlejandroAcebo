package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Temporada;
import application.repository.CapituloRepository;
import application.repository.TemporadaRepository;

@Service
public class CapituloService {
    private final CapituloRepository capituloRepository;
    private final TemporadaRepository temporadaRepository;

    public CapituloService(CapituloRepository capituloRepository, TemporadaRepository temporadaRepository) {
        this.capituloRepository = capituloRepository;
        this.temporadaRepository = temporadaRepository;
    }

    @Transactional(readOnly = true)
    public List<Capitulo> findByTemporadaId(int temporadaId) {
        validarTemporadaExiste(temporadaId);
        return capituloRepository.findByTemporadaIdTemporada(temporadaId);
    }

    @Transactional(readOnly = true)
    public Capitulo findById(int idCapitulo) {
        return getCapituloById(idCapitulo);
    }

    private Capitulo getCapituloById(int idCapitulo) {
        return capituloRepository.findById(idCapitulo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el capítulo con id " + idCapitulo));
    }

    private Temporada validarTemporadaExiste(int idTemporada) {
        return temporadaRepository.findById(idTemporada)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la temporada con id " + idTemporada));
    }
}
