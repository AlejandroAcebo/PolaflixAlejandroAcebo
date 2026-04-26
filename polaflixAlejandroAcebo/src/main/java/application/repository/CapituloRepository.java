package application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import application.model.entity.serie.Capitulo;

public interface CapituloRepository extends JpaRepository<Capitulo, Integer> {
    List<Capitulo> findByTemporadaIdTemporada(int temporadaId);
}
