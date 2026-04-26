package application.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import application.model.dto.factura.FacturaResponseDto;
import application.model.entity.facturacion.Factura;
import application.model.entity.usuario.Usuario;
import application.repository.FacturaRepository;
import application.repository.UsuarioRepository;

@Service
public class FacturaService {
    private final FacturaRepository facturaRepository;
    private final UsuarioRepository usuarioRepository;

    public FacturaService(FacturaRepository facturaRepository, UsuarioRepository usuarioRepository) {
        this.facturaRepository = facturaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<FacturaResponseDto> findByUsuarioId(int usuarioId) {
        validarUsuarioExiste(usuarioId);
        return facturaRepository.findByUsuarioIdUsuario(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FacturaResponseDto> findAll() {
        return facturaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public FacturaResponseDto findById(int idFactura) {
        return toResponse(getFacturaById(idFactura));
    }

    private Factura getFacturaById(int idFactura) {
        return facturaRepository.findById(idFactura)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la factura con id " + idFactura));
    }

    private Usuario validarUsuarioExiste(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el usuario con id " + idUsuario));
    }

    private FacturaResponseDto toResponse(Factura factura) {
        return new FacturaResponseDto(
                factura.getIdFactura(),
                factura.getUsuario().getIdUsuario(),
                factura.getFecha().toString(),
                factura.getTotal()
        );
    }
}
