package application.model.entity.seguimientoserie;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Serie;
import application.model.entity.serie.Temporada;
import application.model.entity.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Visualizacion {
    
    @Id
    @GeneratedValue
    private Integer idVisualizacion;
    
    private LocalDate fechaVisualizacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCapitulo", nullable = false)
    @JsonIgnore
    private Capitulo capitulo;

    @JsonProperty("idUsuario")
    public int getIdUsuario() {
        return usuario == null ? 0 : usuario.getIdUsuario();
    }

    @JsonProperty("idCapitulo")
    public int getIdCapitulo() {
        return capitulo == null ? 0 : capitulo.getIdCapitulo();
    }

    public boolean perteneceAlMes(int anio, int mes) {
        return fechaVisualizacion != null
                && fechaVisualizacion.getYear() == anio
                && fechaVisualizacion.getMonthValue() == mes;
    }

    public int idSerie() {
        Serie serie = serie();
        return serie == null ? 0 : serie.getIdSerie();
    }

    public int idCapitulo() {
        return capitulo == null ? 0 : capitulo.getIdCapitulo();
    }

    public String nombreSerie() {
        Serie serie = serie();
        return serie == null ? "" : serie.getNombreSerie();
    }

    public String episodio() {
        Temporada temporada = temporada();
        if (temporada == null || capitulo == null) {
            return "";
        }
        return temporada.getNumeroTemporada() + "x" + capitulo.getNumeroCapitulo();
    }

    public double precio() {
        Serie serie = serie();
        return serie == null ? 0 : serie.precioPorCapitulo();
    }

    private Temporada temporada() {
        return capitulo == null ? null : capitulo.getTemporada();
    }

    private Serie serie() {
        Temporada temporada = temporada();
        return temporada == null ? null : temporada.getSerie();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Visualizacion visualizacion)) {
            return false;
        }
        return idVisualizacion != null && idVisualizacion.equals(visualizacion.idVisualizacion);
    }

    @Override
    public int hashCode() {
        return Visualizacion.class.hashCode();
    }

}
