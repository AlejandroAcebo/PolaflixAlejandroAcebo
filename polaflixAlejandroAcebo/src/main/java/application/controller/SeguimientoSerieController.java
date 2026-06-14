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

import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.request.SeguimientoSerieRequest;
import application.model.view.ErrorView;
import application.model.view.SeguimientoCreadoView;
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

    @PostMapping
    @Operation(summary = "Agregar una serie a pendientes")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Serie agregada al espacio personal",
                    content = @Content(schema = @Schema(implementation = SeguimientoCreadoView.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud no valida",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o serie no encontrados",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto de datos",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorView.class)))
    })
    public ResponseEntity<SeguimientoCreadoView> createSeguimiento(
            @PathVariable("usuarioId") @Positive int usuarioId,
            @Valid @RequestBody SeguimientoSerieRequest request) {
        SeguimientoSerie createdSeguimiento = seguimientoSerieService.create(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toView(createdSeguimiento));
    }

    private SeguimientoCreadoView toView(SeguimientoSerie seguimiento) {
        return new SeguimientoCreadoView(
                seguimiento.getIdSeguimientoSerie(),
                seguimiento.getIdSerie(),
                seguimiento.getIdCapituloUltimoVisto(),
                seguimiento.getEstadoSerie() == null ? null : seguimiento.getEstadoSerie().name());
    }
}
