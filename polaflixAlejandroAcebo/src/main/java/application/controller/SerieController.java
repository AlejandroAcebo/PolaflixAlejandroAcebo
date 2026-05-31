package application.controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import application.model.entity.serie.Serie;
import application.model.view.Views;
import application.service.SerieService;
import application.service.UsuarioService;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/series")
@Validated
public class SerieController {

    private final SerieService serieService;
    private final UsuarioService usuarioService;

    public SerieController(SerieService serieService, UsuarioService usuarioService) {
        this.serieService = serieService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<Serie> getSeries() {
        return serieService.findAll();
    }

    @GetMapping("/usuario/{usuarioId}")
    @JsonView(Views.Summary.class)
    public List<Serie> getSeriesPorUsuario(@PathVariable("usuarioId") @Positive int usuarioId) {
        usuarioService.findById(usuarioId);
        return serieService.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Detail.class)
    public Serie getSerieById(@PathVariable("id") @Positive int id) {
        return serieService.findById(id);
    }

}
