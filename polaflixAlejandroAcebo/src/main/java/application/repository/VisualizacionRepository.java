package application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.entity.seguimientoserie.Visualizacion;

public interface VisualizacionRepository extends JpaRepository<Visualizacion, Integer> {
    List<Visualizacion> findByUsuarioIdUsuario(int usuarioId);
    List<Visualizacion> findByCapituloIdCapitulo(int capituloId);
    Optional<Visualizacion> findByUsuarioIdUsuarioAndCapituloIdCapitulo(int usuarioId, int capituloId);
}
