package application.model.entity.facturacion;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import application.model.entity.seguimientoserie.Visualizacion;
import application.model.view.Views;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;


@Value
@Embeddable
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Cargo {
    
    @JsonView(Views.Summary.class)
    private LocalDate fecha;
    
    @JsonView(Views.Summary.class)
    private double precio;
    
    @JsonView(Views.Summary.class)
    private String nombreSerie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVisualizacion", nullable = false)
    @JsonIgnore
    private Visualizacion visualizacion;

    public static Cargo desdeVisualizacion(Visualizacion visualizacion) {
        if (visualizacion == null) {
            throw new IllegalArgumentException("El cargo debe estar asociado a una visualizacion");
        }
        return new Cargo(
                visualizacion.getFechaVisualizacion(),
                visualizacion.precio(),
                visualizacion.nombreSerie(),
                visualizacion);
    }

    @JsonProperty("idVisualizacion")
    @JsonView(Views.Summary.class)
    public Integer getIdVisualizacion() {
        return visualizacion == null ? null : visualizacion.getIdVisualizacion();
    }

    @JsonProperty("idCapitulo")
    @JsonView(Views.Summary.class)
    public Integer getIdCapitulo() {
        return visualizacion == null ? null : visualizacion.idCapitulo();
    }

    @JsonProperty("idTemporada")
    @JsonView(Views.Summary.class)
    public Integer getIdTemporada() {
        return visualizacion == null ? null : visualizacion.idTemporada();
    }

}
