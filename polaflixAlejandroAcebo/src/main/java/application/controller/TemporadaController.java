package application.controller;

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
import org.springframework.web.bind.annotation.RestController;

import application.model.dto.temporada.TemporadaRequestDto;
import application.model.dto.temporada.TemporadaResponseDto;
import application.service.TemporadaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/series/{serieId}/temporadas")
@Validated
public class TemporadaController {

    private final TemporadaService temporadaService;

    public TemporadaController(TemporadaService temporadaService) {
        this.temporadaService = temporadaService;
    }

    @GetMapping
    public List<TemporadaResponseDto> getTemporadas(@PathVariable int serieId) {
        return temporadaService.findBySerieId(serieId);
    }

    @GetMapping("/{id}")
    public TemporadaResponseDto getTemporadaById(@PathVariable int serieId, @PathVariable int id) {
        return temporadaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TemporadaResponseDto> createTemporada(
            @PathVariable int serieId,
            @Valid @RequestBody TemporadaRequestDto request) {
        TemporadaResponseDto createdTemporada = temporadaService.create(request);
        return ResponseEntity.created(URI.create("/series/" + serieId + "/temporadas/" + createdTemporada.getIdTemporada())).body(createdTemporada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemporadaResponseDto> updateTemporada(
            @PathVariable int serieId,
            @PathVariable int id,
            @Valid @RequestBody TemporadaRequestDto request) {
        TemporadaResponseDto updatedTemporada = temporadaService.update(id, request);
        return ResponseEntity.ok(updatedTemporada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemporada(@PathVariable int serieId, @PathVariable int id) {
        temporadaService.delete(id);
        return ResponseEntity.ok().build();
    }
}
