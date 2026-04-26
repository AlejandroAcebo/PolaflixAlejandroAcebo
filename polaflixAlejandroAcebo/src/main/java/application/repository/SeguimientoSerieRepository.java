package application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.entity.seguimientoserie.SeguimientoSerie;

public interface SeguimientoSerieRepository extends JpaRepository<SeguimientoSerie, Integer> {
    List<SeguimientoSerie> findByUsuarioIdUsuario(int usuarioId);
    Optional<SeguimientoSerie> findByUsuarioIdUsuarioAndSerieIdSerie(int usuarioId, int serieId);
}
