package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.facturacion.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
}
