package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.serie.Serie;

public interface SerieRepository extends JpaRepository<Serie, Integer> {
}
