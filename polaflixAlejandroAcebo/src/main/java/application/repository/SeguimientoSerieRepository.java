package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.seguimientoserie.SeguimientoSerie;

public interface SeguimientoSerieRepository extends JpaRepository<SeguimientoSerie, Integer> {
}
