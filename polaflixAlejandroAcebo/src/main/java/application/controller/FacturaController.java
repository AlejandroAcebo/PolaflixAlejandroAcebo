package application.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.dto.factura.FacturaResponseDto;
import application.service.FacturaService;

@RestController
@RequestMapping("/facturas")
@Validated
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public List<FacturaResponseDto> getFacturas() {
        return facturaService.findAll();
    }

    @GetMapping("/{id}")
    public FacturaResponseDto getFacturaById(@PathVariable int id) {
        return facturaService.findById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<FacturaResponseDto> getFacturasByUsuario(@PathVariable int usuarioId) {
        return facturaService.findByUsuarioId(usuarioId);
    }
}
