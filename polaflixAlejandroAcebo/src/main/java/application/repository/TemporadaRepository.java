package application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.entity.serie.Temporada;

public interface TemporadaRepository extends JpaRepository<Temporada, Integer> {
    List<Temporada> findBySerieIdSerie(int serieId);
}
