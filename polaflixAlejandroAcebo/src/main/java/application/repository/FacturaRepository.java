package application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.entity.facturacion.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    List<Factura> findByUsuarioIdUsuario(int usuarioId);
}
