package application.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import application.model.dto.serie.SerieRequestDto;
import application.model.dto.serie.SerieResponseDto;
import application.model.entity.serie.Serie;
import application.repository.SerieRepository;
import io.micrometer.common.util.StringUtils;

@Service
public class SerieService {
    private final SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @Transactional(readOnly = true)
    public List<SerieResponseDto> findAll() {
        return serieRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SerieResponseDto findById(int idSerie) {
        return toResponse(getSerieById(idSerie));
    }

    @Transactional
    public SerieResponseDto create(SerieRequestDto request) {
        if (StringUtils.isBlank(request.getNombreSerie())) {
            throw new IllegalArgumentException("El nombre de la serie no puede estar vacío");
        }

        Serie serie = Serie.builder()
                .nombreSerie(request.getNombreSerie())
                .sinopsis(request.getSinopsis())
                .tipoSerie(null)
                .build();

        return toResponse(serieRepository.save(serie));
    }

    @Transactional
    public SerieResponseDto update(int idSerie, SerieRequestDto request) {
        Serie serie = getSerieById(idSerie);

        if (StringUtils.isNotBlank(request.getNombreSerie())) {
            serie.setNombreSerie(request.getNombreSerie());
        }
        if (StringUtils.isNotBlank(request.getSinopsis())) {
            serie.setSinopsis(request.getSinopsis());
        }

        return toResponse(serieRepository.save(serie));
    }

    @Transactional
    public void delete(int idSerie) {
        Serie serie = getSerieById(idSerie);
        serieRepository.delete(serie);
    }

    private Serie getSerieById(int idSerie) {
        return serieRepository.findById(idSerie)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la serie con id " + idSerie));
    }

    private SerieResponseDto toResponse(Serie serie) {
        return new SerieResponseDto(
                serie.getIdSerie(),
                serie.getNombreSerie(),
                serie.getSinopsis(),
                serie.getTipoSerie() != null ? serie.getTipoSerie().toString() : "DESCONOCIDO"
        );
    }
}
