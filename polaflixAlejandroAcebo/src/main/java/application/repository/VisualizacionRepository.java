package application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.entity.seguimientoserie.Visualizacion;

public interface VisualizacionRepository extends JpaRepository<Visualizacion, Integer> {
    List<Visualizacion> findByUsuarioIdUsuario(int usuarioId);
    List<Visualizacion> findByCapituloIdCapitulo(int capituloId);
}
