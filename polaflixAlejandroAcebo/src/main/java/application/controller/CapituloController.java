package application.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.entity.serie.Capitulo;
import application.model.view.Views;
import application.service.CapituloService;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/series/{serieId}/temporadas/{temporadaId}/capitulos")
@Validated
public class CapituloController {

    private final CapituloService capituloService;

    public CapituloController(CapituloService capituloService) {
        this.capituloService = capituloService;
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<Capitulo> getCapitulos(
            @PathVariable("serieId") @Positive int serieId,
            @PathVariable("temporadaId") @Positive int temporadaId) {
        return capituloService.findByTemporadaId(temporadaId);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Detail.class)
    public Capitulo getCapituloById(
            @PathVariable("serieId") @Positive int serieId,
            @PathVariable("temporadaId") @Positive int temporadaId,
            @PathVariable("id") @Positive int id) {
        return capituloService.findById(id);
    }

}
