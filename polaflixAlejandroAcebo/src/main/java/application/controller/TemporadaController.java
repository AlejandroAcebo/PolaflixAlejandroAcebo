package application.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.entity.serie.Temporada;
import application.model.view.Views;
import application.service.TemporadaService;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/series/{serieId}/temporadas")
@Validated
public class TemporadaController {

    private final TemporadaService temporadaService;

    public TemporadaController(TemporadaService temporadaService) {
        this.temporadaService = temporadaService;
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<Temporada> getTemporadas(@PathVariable("serieId") @Positive int serieId) {
        return temporadaService.findBySerieId(serieId);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Detail.class)
    public Temporada getTemporadaById(
            @PathVariable("serieId") @Positive int serieId,
            @PathVariable("id") @Positive int id) {
        return temporadaService.findById(id);
    }

}
