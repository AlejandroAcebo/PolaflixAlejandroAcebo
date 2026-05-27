package application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.exception.ResourceNotFoundException;
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
    public List<Factura> findByUsuarioId(int usuarioId) {
        validarUsuarioExiste(usuarioId);
        return facturaRepository.findByUsuarioIdUsuario(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Factura findById(int idFactura) {
        return getFacturaById(idFactura);
    }

    private Factura getFacturaById(int idFactura) {
        return facturaRepository.findById(idFactura)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe la factura con id " + idFactura));
    }

    private Usuario validarUsuarioExiste(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el usuario con id " + idUsuario));
    }
}
