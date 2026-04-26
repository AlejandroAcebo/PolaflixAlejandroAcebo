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

import application.model.dto.visualizacion.VisualizacionRequestDto;
import application.model.dto.visualizacion.VisualizacionResponseDto;
import application.service.VisualizacionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios/{usuarioId}/visualizaciones")
@Validated
public class VisualizacionController {

    private final VisualizacionService visualizacionService;

    public VisualizacionController(VisualizacionService visualizacionService) {
        this.visualizacionService = visualizacionService;
    }

    @GetMapping
    public List<VisualizacionResponseDto> getVisualizaciones(@PathVariable int usuarioId) {
        return visualizacionService.findByUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    public VisualizacionResponseDto getVisualizacionById(
            @PathVariable int usuarioId,
            @PathVariable int id) {
        return visualizacionService.findById(id);
    }

    @PostMapping
    public ResponseEntity<VisualizacionResponseDto> createVisualizacion(
            @PathVariable int usuarioId,
            @Valid @RequestBody VisualizacionRequestDto request) {
        VisualizacionResponseDto createdVisualizacion = visualizacionService.create(usuarioId, request);
        return ResponseEntity.created(URI.create("/usuarios/" + usuarioId + "/visualizaciones/" + createdVisualizacion.getIdVisualizacion())).body(createdVisualizacion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisualizacion(
            @PathVariable int usuarioId,
            @PathVariable int id) {
        visualizacionService.delete(id);
        return ResponseEntity.ok().build();
    }
}
