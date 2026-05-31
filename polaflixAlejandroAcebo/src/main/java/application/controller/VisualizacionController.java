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

import application.model.entity.seguimientoserie.Visualizacion;
import application.model.request.VisualizacionRequest;
import application.model.view.Views;
import application.service.VisualizacionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/usuarios/{usuarioId}/visualizaciones")
@Validated
public class VisualizacionController {

    private final VisualizacionService visualizacionService;

    public VisualizacionController(VisualizacionService visualizacionService) {
        this.visualizacionService = visualizacionService;
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<Visualizacion> getVisualizaciones(@PathVariable("usuarioId") @Positive int usuarioId) {
        return visualizacionService.findByUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Detail.class)
    public Visualizacion getVisualizacionById(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @PathVariable("id") @Positive int id) {
        return visualizacionService.findById(id);
    }

    @PostMapping
    @JsonView(Views.Detail.class)
    public ResponseEntity<Visualizacion> createVisualizacion(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @Valid @RequestBody VisualizacionRequest request) {
        Visualizacion createdVisualizacion = visualizacionService.create(usuarioId, request);
        return ResponseEntity.created(URI.create("/usuarios/" + usuarioId + "/visualizaciones/" + createdVisualizacion.getIdVisualizacion())).body(createdVisualizacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisualizacion(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @PathVariable("id") @Positive int id) {
        visualizacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
