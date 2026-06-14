package application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.entity.seguimientoserie.Visualizacion;
import application.model.request.VisualizacionRequest;
import application.model.view.ErrorView;
import application.model.view.VisualizacionRegistradaView;
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

    @PostMapping
    @Operation(summary = "Marcar un capitulo como visto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Visualizacion registrada",
                    content = @Content(schema = @Schema(implementation = VisualizacionRegistradaView.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud no valida",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "404", description = "Usuario, capitulo o seguimiento no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorView.class)))
    })
    public ResponseEntity<VisualizacionRegistradaView> createVisualizacion(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @Valid @RequestBody VisualizacionRequest request) {
        Visualizacion createdVisualizacion = visualizacionService.create(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toView(createdVisualizacion));
    }

    private VisualizacionRegistradaView toView(Visualizacion visualizacion) {
        return new VisualizacionRegistradaView(
                visualizacion.getIdVisualizacion(),
                visualizacion.getFechaVisualizacion().toString(),
                visualizacion.getIdUsuario(),
                visualizacion.getIdCapitulo(),
                visualizacion.idTemporada(),
                visualizacion.idSerie());
    }
}
