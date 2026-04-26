package application.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import application.model.dto.temporada.TemporadaRequestDto;
import application.model.dto.temporada.TemporadaResponseDto;
import application.model.entity.serie.Serie;
import application.model.entity.serie.Temporada;
import application.repository.SerieRepository;
import application.repository.TemporadaRepository;
import io.micrometer.common.util.StringUtils;

@Service
public class TemporadaService {
    private final TemporadaRepository temporadaRepository;
    private final SerieRepository serieRepository;

    public TemporadaService(TemporadaRepository temporadaRepository, SerieRepository serieRepository) {
        this.temporadaRepository = temporadaRepository;
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public List<TemporadaResponseDto> findAll() {
        return temporadaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TemporadaResponseDto> findBySerieId(int serieId) {
        validarSerieExiste(serieId);
        return temporadaRepository.findBySerieIdSerie(serieId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TemporadaResponseDto findById(int idTemporada) {
        return toResponse(getTemporadaById(idTemporada));
    }

    @Transactional
    public TemporadaResponseDto create(TemporadaRequestDto request) {
        if (StringUtils.isBlank(request.getNombreTemporada())) {
            throw new IllegalArgumentException("El nombre de la temporada no puede estar vacío");
        }
        if (request.getNumeroTemporada() <= 0) {
            throw new IllegalArgumentException("El número de la temporada debe ser positivo");
        }

        Serie serie = validarSerieExiste(request.getIdSerie());

        Temporada temporada = Temporada.builder()
                .serie(serie)
                .nombreTemporada(request.getNombreTemporada())
                .numeroTemporada(request.getNumeroTemporada())
                .build();

        return toResponse(temporadaRepository.save(temporada));
    }

    @Transactional
    public TemporadaResponseDto update(int idTemporada, TemporadaRequestDto request) {
        Temporada temporada = getTemporadaById(idTemporada);

        if (StringUtils.isNotBlank(request.getNombreTemporada())) {
            temporada.setNombreTemporada(request.getNombreTemporada());
        }
        if (request.getNumeroTemporada() > 0) {
            temporada.setNumeroTemporada(request.getNumeroTemporada());
        }

        return toResponse(temporadaRepository.save(temporada));
    }

    @Transactional
    public void delete(int idTemporada) {
        Temporada temporada = getTemporadaById(idTemporada);
        temporadaRepository.delete(temporada);
    }

    private Temporada getTemporadaById(int idTemporada) {
        return temporadaRepository.findById(idTemporada)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la temporada con id " + idTemporada));
    }

    private Serie validarSerieExiste(int idSerie) {
        return serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la serie con id " + idSerie));
    }

    private TemporadaResponseDto toResponse(Temporada temporada) {
        return new TemporadaResponseDto(
                temporada.getIdTemporada(),
                temporada.getSerie().getIdSerie(),
                temporada.getNombreTemporada(),
                temporada.getNumeroTemporada()
        );
    }
}
