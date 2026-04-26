package application.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.dto.seguimientoserie.SeguimientoSerieRequestDto;
import application.model.dto.seguimientoserie.SeguimientoSerieResponseDto;
import application.service.SeguimientoSerieService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios/{usuarioId}/seguimientos")
@Validated
public class SeguimientoSerieController {

    private final SeguimientoSerieService seguimientoSerieService;

    public SeguimientoSerieController(SeguimientoSerieService seguimientoSerieService) {
        this.seguimientoSerieService = seguimientoSerieService;
    }

    @GetMapping
    public List<SeguimientoSerieResponseDto> getSeguimientos(@PathVariable int usuarioId) {
        return seguimientoSerieService.findByUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    public SeguimientoSerieResponseDto getSeguimientoById(
            @PathVariable int usuarioId,
            @PathVariable int id) {
        return seguimientoSerieService.findById(id);
    }

    @PostMapping
    public ResponseEntity<SeguimientoSerieResponseDto> createSeguimiento(
            @PathVariable int usuarioId,
            @Valid @RequestBody SeguimientoSerieRequestDto request) {
        SeguimientoSerieResponseDto createdSeguimiento = seguimientoSerieService.create(usuarioId, request);
        return ResponseEntity.created(URI.create("/usuarios/" + usuarioId + "/seguimientos/" + createdSeguimiento.getIdSeguimientoSerie())).body(createdSeguimiento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeguimiento(
            @PathVariable int usuarioId,
            @PathVariable int id) {
        seguimientoSerieService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/series/{serieId}")
    public ResponseEntity<Void> deleteSeguimientoBySerie(
            @PathVariable int usuarioId,
            @PathVariable int serieId) {
        seguimientoSerieService.deleteBySerie(usuarioId, serieId);
        return ResponseEntity.ok().build();
    }
}
