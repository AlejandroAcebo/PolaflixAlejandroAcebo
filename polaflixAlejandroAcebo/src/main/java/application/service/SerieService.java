package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.model.dto.serie.SerieResponseDto;
import application.model.dto.usuario.UsuarioResponseDto;
import application.model.entity.serie.Serie;
import application.model.entity.usuario.Usuario;
import application.repository.SerieRepository;

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

    private SerieResponseDto toResponse(Serie serie) {
        return new SerieResponseDto(// TODO: completar con los campos necesarios
        );
    }

}
