package application.controller;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.request.SeguimientoSerieRequest;
import application.model.view.Views;
import application.service.SeguimientoSerieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/usuarios/{usuarioId}/seguimientos")
@Validated
public class SeguimientoSerieController {

    private final SeguimientoSerieService seguimientoSerieService;

    public SeguimientoSerieController(SeguimientoSerieService seguimientoSerieService) {
        this.seguimientoSerieService = seguimientoSerieService;
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<SeguimientoSerie> getSeguimientos(@PathVariable("usuarioId") @Positive int usuarioId) {
        return seguimientoSerieService.findByUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Detail.class)
    public SeguimientoSerie getSeguimientoById(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @PathVariable("id") @Positive int id) {
        return seguimientoSerieService.findById(id);
    }

    @PostMapping
    @JsonView(Views.Detail.class)
    public ResponseEntity<SeguimientoSerie> createSeguimiento(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @Valid @RequestBody SeguimientoSerieRequest request) {
        SeguimientoSerie createdSeguimiento = seguimientoSerieService.create(usuarioId, request);
        return ResponseEntity.created(URI.create("/usuarios/" + usuarioId + "/seguimientos/" + createdSeguimiento.getIdSeguimientoSerie())).body(createdSeguimiento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeguimiento(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @PathVariable("id") @Positive int id) {
        seguimientoSerieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/series/{serieId}")
    public ResponseEntity<Void> deleteSeguimientoBySerie(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @PathVariable("serieId") @Positive int serieId) {
        seguimientoSerieService.deleteBySerie(usuarioId, serieId);
        return ResponseEntity.noContent().build();
    }
}
