package application.controller;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import application.model.dto.serie.SerieRequestDto;
import application.model.dto.serie.SerieResponseDto;
import application.service.SerieService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/series")
@Validated
public class SerieController {

    private final SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping
    public List<SerieResponseDto> getSeries() {
        return serieService.findAll();
    }

    @GetMapping("/{id}")
    public SerieResponseDto getSerieById(@PathVariable int id) {
        return serieService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SerieResponseDto> createSerie(@Valid @RequestBody SerieRequestDto request) {
        SerieResponseDto createdSerie = serieService.create(request);
        return ResponseEntity.created(URI.create("/series/" + createdSerie.getIdSerie())).body(createdSerie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SerieResponseDto> updateSerie(@PathVariable int id, @Valid @RequestBody SerieRequestDto request) {
        SerieResponseDto updatedSerie = serieService.update(id, request);
        return ResponseEntity.ok(updatedSerie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSerie(@PathVariable int id) {
        serieService.delete(id);
        return ResponseEntity.ok().build();
    }
}
