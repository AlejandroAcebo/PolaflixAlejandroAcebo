package application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import application.model.view.CatalogoView;
import application.model.view.ErrorView;
import application.model.view.FacturaMensualView;
import application.model.view.SerieCatalogoView;
import application.model.view.SerieDetalleView;
import application.model.view.UsuarioHomeView;
import application.service.PantallaUsuarioService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioController {

    private final PantallaUsuarioService pantallaUsuarioService;

    public UsuarioController(PantallaUsuarioService pantallaUsuarioService) {
        this.pantallaUsuarioService = pantallaUsuarioService;
    }

    @GetMapping("/{id}/home")
    @Operation(summary = "Obtener la pantalla inicial del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pantalla inicial del usuario",
                    content = @Content(schema = @Schema(implementation = UsuarioHomeView.class))),
            @ApiResponse(responseCode = "400", description = "Identificador de usuario no valido",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorView.class)))
    })
    public UsuarioHomeView getHome(@PathVariable("id") @Positive int id) {
        return pantallaUsuarioService.getHome(id);
    }

    @GetMapping("/{id}/series/{serieId}/detalle")
    @Operation(summary = "Obtener el detalle de una serie del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de la serie",
                    content = @Content(schema = @Schema(implementation = SerieDetalleView.class))),
            @ApiResponse(responseCode = "400", description = "Parametros no validos",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o serie no encontrados",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorView.class)))
    })
    public SerieDetalleView getSerieDetalle(
            @PathVariable("id") @Positive int id,
            @PathVariable("serieId") @Positive int serieId) {
        return pantallaUsuarioService.getSerieDetalle(id, serieId);
    }

    @GetMapping("/{id}/catalogo-buscar")
    @Operation(summary = "Buscar una serie en el catalogo del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Serie encontrada",
                    content = @Content(schema = @Schema(implementation = SerieCatalogoView.class))),
            @ApiResponse(responseCode = "400", description = "Busqueda no valida",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "404", description = "Usuario o serie no encontrados",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorView.class)))
    })
    public SerieCatalogoView buscarSerieCatalogo(
            @PathVariable("id") @Positive int id,
            @RequestParam("nombre") @NotBlank String nombre) {
        return pantallaUsuarioService.buscarSerieCatalogo(id, nombre);
    }

    @GetMapping("/{id}/catalogo/{inicial:[A-Za-z0-9]}")
    @Operation(summary = "Obtener catalogo de series por inicial")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catalogo por inicial",
                    content = @Content(schema = @Schema(implementation = CatalogoView.class))),
            @ApiResponse(responseCode = "400", description = "Parametros no validos",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorView.class)))
    })
    public CatalogoView getCatalogo(
            @PathVariable("id") @Positive int id,
            @PathVariable("inicial") @NotBlank String inicial) {
        return pantallaUsuarioService.getCatalogo(id, inicial);
    }

    @GetMapping("/{id}/facturas/{anio}/{mes}")
    @Operation(summary = "Obtener factura mensual del usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura mensual",
                    content = @Content(schema = @Schema(implementation = FacturaMensualView.class))),
            @ApiResponse(responseCode = "400", description = "Parametros no validos",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorView.class))),
            @ApiResponse(responseCode = "500", description = "Error interno",
                    content = @Content(schema = @Schema(implementation = ErrorView.class)))
    })
    public FacturaMensualView getFacturaMensual(
            @PathVariable("id") @Positive int id,
            @PathVariable("anio") @Positive int anio,
            @PathVariable("mes") @Min(1) @Max(12) int mes) {
        return pantallaUsuarioService.getFacturaMensual(id, anio, mes);
    }
}
