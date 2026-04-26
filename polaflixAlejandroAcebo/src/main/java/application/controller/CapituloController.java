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

import application.model.dto.capitulo.CapituloRequestDto;
import application.model.dto.capitulo.CapituloResponseDto;
import application.service.CapituloService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/series/{serieId}/temporadas/{temporadaId}/capitulos")
@Validated
public class CapituloController {

    private final CapituloService capituloService;

    public CapituloController(CapituloService capituloService) {
        this.capituloService = capituloService;
    }

    @GetMapping
    public List<CapituloResponseDto> getCapitulos(
            @PathVariable int serieId,
            @PathVariable int temporadaId) {
        return capituloService.findByTemporadaId(temporadaId);
    }

    @GetMapping("/{id}")
    public CapituloResponseDto getCapituloById(
            @PathVariable int serieId,
            @PathVariable int temporadaId,
            @PathVariable int id) {
        return capituloService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CapituloResponseDto> createCapitulo(
            @PathVariable int serieId,
            @PathVariable int temporadaId,
            @Valid @RequestBody CapituloRequestDto request) {
        CapituloResponseDto createdCapitulo = capituloService.create(request);
        return ResponseEntity.created(URI.create("/series/" + serieId + "/temporadas/" + temporadaId + "/capitulos/" + createdCapitulo.getIdCapitulo())).body(createdCapitulo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CapituloResponseDto> updateCapitulo(
            @PathVariable int serieId,
            @PathVariable int temporadaId,
            @PathVariable int id,
            @Valid @RequestBody CapituloRequestDto request) {
        CapituloResponseDto updatedCapitulo = capituloService.update(id, request);
        return ResponseEntity.ok(updatedCapitulo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCapitulo(
            @PathVariable int serieId,
            @PathVariable int temporadaId,
            @PathVariable int id) {
        capituloService.delete(id);
        return ResponseEntity.ok().build();
    }
}
