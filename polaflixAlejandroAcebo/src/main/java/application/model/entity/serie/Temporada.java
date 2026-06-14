package application.model.entity.serie;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import application.model.view.Views;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Temporada {

    @Id
    @GeneratedValue
    @JsonView(Views.Summary.class)
    private int idTemporada;
    
    @ManyToOne
    @JoinColumn(name = "idSerie", nullable = false)
    @JsonIgnore
    private Serie serie;
    
    @JsonView(Views.Summary.class)
    private String nombreTemporada;
    
    @JsonView(Views.Summary.class)
    private int numeroTemporada;
    
    @OneToMany(mappedBy = "temporada", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Capitulo> capitulos;

    public static Temporada crear(Serie serie, String nombreTemporada, int numeroTemporada) {
        return Temporada.builder()
                .serie(serie)
                .nombreTemporada(nombreTemporada)
                .numeroTemporada(numeroTemporada)
                .build();
    }

    @JsonProperty("idSerie")
    @JsonView(Views.Summary.class)
    public int getIdSerie() {
        return serie == null ? 0 : serie.getIdSerie();
    }

    public List<Capitulo> capitulosOrdenados() {
        if (capitulos == null) {
            return List.of();
        }
        return capitulos.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(Capitulo::getNumeroCapitulo))
                .toList();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Temporada temporada)) {
            return false;
        }
        return idTemporada != 0 && idTemporada == temporada.idTemporada;
    }

    @Override
    public int hashCode() {
        return Temporada.class.hashCode();
    }
}
