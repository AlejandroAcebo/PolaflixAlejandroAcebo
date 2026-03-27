package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.seguimientoserie.Visualizacion;

public interface VisualizacionRepository extends JpaRepository<Visualizacion, Integer> {
}
