package application.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.entity.facturacion.Factura;
import application.model.view.Views;
import application.service.FacturaService;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/facturas")
@Validated
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public List<Factura> getFacturas() {
        return facturaService.findAll();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Detail.class)
    public Factura getFacturaById(@PathVariable("id") @Positive int id) {
        return facturaService.findById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    @JsonView(Views.Detail.class)
    public List<Factura> getFacturasByUsuario(@PathVariable("usuarioId") @Positive int usuarioId) {
        return facturaService.findByUsuarioId(usuarioId);
    }
}
