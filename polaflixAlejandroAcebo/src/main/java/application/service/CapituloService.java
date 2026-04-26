package application.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import application.model.dto.capitulo.CapituloRequestDto;
import application.model.dto.capitulo.CapituloResponseDto;
import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Temporada;
import application.repository.CapituloRepository;
import application.repository.TemporadaRepository;
import io.micrometer.common.util.StringUtils;

@Service
public class CapituloService {
    private final CapituloRepository capituloRepository;
    private final TemporadaRepository temporadaRepository;

    public CapituloService(CapituloRepository capituloRepository, TemporadaRepository temporadaRepository) {
        this.capituloRepository = capituloRepository;
        this.temporadaRepository = temporadaRepository;
    }

    @Transactional(readOnly = true)
    public List<CapituloResponseDto> findAll() {
        return capituloRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CapituloResponseDto> findByTemporadaId(int temporadaId) {
        validarTemporadaExiste(temporadaId);
        return capituloRepository.findByTemporadaIdTemporada(temporadaId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CapituloResponseDto findById(int idCapitulo) {
        return toResponse(getCapituloById(idCapitulo));
    }

    @Transactional
    public CapituloResponseDto create(CapituloRequestDto request) {
        if (StringUtils.isBlank(request.getNombreCapitulo())) {
            throw new IllegalArgumentException("El nombre del capítulo no puede estar vacío");
        }
        if (request.getNumeroCapitulo() <= 0) {
            throw new IllegalArgumentException("El número del capítulo debe ser positivo");
        }
        if (StringUtils.isBlank(request.getEnlace())) {
            throw new IllegalArgumentException("El enlace del capítulo no puede estar vacío");
        }

        Temporada temporada = validarTemporadaExiste(request.getIdTemporada());

        Capitulo capitulo = Capitulo.builder()
                .temporada(temporada)
                .nombreCapitulo(request.getNombreCapitulo())
                .numeroCapitulo(request.getNumeroCapitulo())
                .enlace(request.getEnlace())
                .descripcion(request.getDescripcion())
                .build();

        return toResponse(capituloRepository.save(capitulo));
    }

    @Transactional
    public CapituloResponseDto update(int idCapitulo, CapituloRequestDto request) {
        Capitulo capitulo = getCapituloById(idCapitulo);

        if (StringUtils.isNotBlank(request.getNombreCapitulo())) {
            capitulo.setNombreCapitulo(request.getNombreCapitulo());
        }
        if (request.getNumeroCapitulo() > 0) {
            capitulo.setNumeroCapitulo(request.getNumeroCapitulo());
        }
        if (StringUtils.isNotBlank(request.getEnlace())) {
            capitulo.setEnlace(request.getEnlace());
        }
        if (StringUtils.isNotBlank(request.getDescripcion())) {
            capitulo.setDescripcion(request.getDescripcion());
        }

        return toResponse(capituloRepository.save(capitulo));
    }

    @Transactional
    public void delete(int idCapitulo) {
        Capitulo capitulo = getCapituloById(idCapitulo);
        capituloRepository.delete(capitulo);
    }

    private Capitulo getCapituloById(int idCapitulo) {
        return capituloRepository.findById(idCapitulo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el capítulo con id " + idCapitulo));
    }

    private Temporada validarTemporadaExiste(int idTemporada) {
        return temporadaRepository.findById(idTemporada)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la temporada con id " + idTemporada));
    }

    private CapituloResponseDto toResponse(Capitulo capitulo) {
        return new CapituloResponseDto(
                capitulo.getIdCapitulo(),
                capitulo.getTemporada().getIdTemporada(),
                capitulo.getNombreCapitulo(),
                capitulo.getNumeroCapitulo(),
                capitulo.getEnlace(),
                capitulo.getDescripcion()
        );
    }
}
