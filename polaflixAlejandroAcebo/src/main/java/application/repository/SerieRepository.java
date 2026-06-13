package application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.model.entity.serie.Serie;

public interface SerieRepository extends JpaRepository<Serie, Integer> {
    @Query("""
            select distinct serie
            from Serie serie
            join serie.temporadas temporada
            join temporada.capitulos capitulo
            where capitulo.idCapitulo = :idCapitulo
            """)
    Optional<Serie> findByCapituloId(@Param("idCapitulo") int idCapitulo);
}
