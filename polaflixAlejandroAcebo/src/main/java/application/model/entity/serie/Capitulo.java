package application.model.entity.serie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import application.model.view.Views;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Capitulo {

    @Id
    @GeneratedValue
    @JsonView(Views.Summary.class)
    private int idCapitulo;
    
    @JsonView(Views.Summary.class)
    private String nombreCapitulo;
    
    @JsonView(Views.Summary.class)
    private int numeroCapitulo;
    
    @JsonView(Views.Summary.class)
    private String enlace;
    
    @JsonView(Views.Summary.class)
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "idTemporada", nullable = false)
    @JsonIgnore
    private Temporada temporada;

    public static Capitulo crear(
            Temporada temporada,
            String nombreCapitulo,
            int numeroCapitulo,
            String enlace,
            String descripcion) {
        return Capitulo.builder()
                .temporada(temporada)
                .nombreCapitulo(nombreCapitulo)
                .numeroCapitulo(numeroCapitulo)
                .enlace(enlace)
                .descripcion(descripcion)
                .build();
    }

    @JsonProperty("idTemporada")
    @JsonView(Views.Summary.class)
    public int getIdTemporada() {
        return temporada == null ? 0 : temporada.getIdTemporada();
    }

    public boolean perteneceA(Serie serie) {
        return serie != null
                && temporada != null
                && temporada.getSerie() != null
                && temporada.getSerie().equals(serie);
    }

    public boolean esPosteriorA(Capitulo otro) {
        return compararOrdenNarrativo(otro) > 0;
    }

    public int compararOrdenNarrativo(Capitulo otro) {
        if (otro == null) {
            return 1;
        }

        int temporadaActual = temporada == null ? 0 : temporada.getNumeroTemporada();
        int temporadaOtra = otro.temporada == null ? 0 : otro.temporada.getNumeroTemporada();
        int comparacionTemporada = Integer.compare(temporadaActual, temporadaOtra);
        return comparacionTemporada != 0
                ? comparacionTemporada
                : Integer.compare(numeroCapitulo, otro.numeroCapitulo);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Capitulo capitulo)) {
            return false;
        }
        return idCapitulo != 0 && idCapitulo == capitulo.idCapitulo;
    }

    @Override
    public int hashCode() {
        return Capitulo.class.hashCode();
    }
}
