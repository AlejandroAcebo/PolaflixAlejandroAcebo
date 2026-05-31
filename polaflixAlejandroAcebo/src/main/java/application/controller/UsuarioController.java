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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.entity.usuario.Usuario;
import application.model.request.UsuarioRequest;
import application.model.request.UsuarioUpdateRequest;
import application.model.view.CatalogoView;
import application.model.view.FacturaMensualView;
import application.model.view.SerieCatalogoView;
import application.model.view.SerieDetalleView;
import application.model.view.UsuarioHomeView;
import application.model.view.Views;
import application.service.PantallaUsuarioService;
import application.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PantallaUsuarioService pantallaUsuarioService;

    public UsuarioController(UsuarioService usuarioService, PantallaUsuarioService pantallaUsuarioService) {
        this.usuarioService = usuarioService;
        this.pantallaUsuarioService = pantallaUsuarioService;
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<Usuario> getUsuarios() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Detail.class)
    public Usuario getUsuarioById(@PathVariable("id") @Positive int id) {
        return usuarioService.findById(id);
    }

    @GetMapping("/{id}/home")
    public UsuarioHomeView getHome(@PathVariable("id") @Positive int id) {
        return pantallaUsuarioService.getHome(id);
    }

    @GetMapping("/{id}/series/{serieId}/detalle")
    public SerieDetalleView getSerieDetalle(
            @PathVariable("id") @Positive int id,
            @PathVariable("serieId") @Positive int serieId) {
        return pantallaUsuarioService.getSerieDetalle(id, serieId);
    }

    @GetMapping("/{id}/catalogo/{inicial}")
    public CatalogoView getCatalogo(
            @PathVariable("id") @Positive int id,
            @PathVariable("inicial") @NotBlank String inicial) {
        return pantallaUsuarioService.getCatalogo(id, inicial);
    }

    @GetMapping("/{id}/catalogo/buscar/{nombre}")
    public SerieCatalogoView buscarSerieCatalogo(
            @PathVariable("id") @Positive int id,
            @PathVariable("nombre") @NotBlank String nombre) {
        return pantallaUsuarioService.buscarSerieCatalogo(id, nombre);
    }

    @GetMapping("/{id}/facturas/{anio}/{mes}")
    public FacturaMensualView getFacturaMensual(
            @PathVariable("id") @Positive int id,
            @PathVariable("anio") @Positive int anio,
            @PathVariable("mes") @Min(1) @Max(12) int mes) {
        return pantallaUsuarioService.getFacturaMensual(id, anio, mes);
    }

    @PostMapping
    @JsonView(Views.Detail.class)
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody UsuarioRequest request) {
        Usuario createdUsuario = usuarioService.create(request);
        return ResponseEntity.created(URI.create("/usuarios/" + createdUsuario.getIdUsuario())).body(createdUsuario);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Detail.class)
    public ResponseEntity<Usuario> updateUsuario(@PathVariable("id") @Positive int id, @Valid @RequestBody UsuarioUpdateRequest request) {
        Usuario updatedUsuario = usuarioService.update(id, request);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable("id") @Positive int id) {
        usuarioService.delete(id);
        return ResponseEntity.ok().build();
    }
}
