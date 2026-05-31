package application.model.entity.seguimientoserie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Serie;
import application.model.entity.usuario.Usuario;
import application.model.enums.EstadoSerie;
import application.model.view.Views;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idUsuario", "idSerie"}) 
})
public class SeguimientoSerie {

    @Id
    @GeneratedValue
    @JsonView(Views.Summary.class)
    private int idSeguimientoSerie;
    
    @ManyToOne
    @JoinColumn(name = "idSerie", nullable = false)
    @JsonIgnore
    private Serie serie;
    
    @ManyToOne
    @JoinColumn(name = "idCapitulo", nullable = true)
    @JsonIgnore
    private Capitulo ultimoVisto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;
    
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Summary.class)
    private EstadoSerie estadoSerie;

    @JsonProperty("idSerie")
    @JsonView(Views.Summary.class)
    public int getIdSerie() {
        return serie == null ? 0 : serie.getIdSerie();
    }

    @JsonProperty("idCapituloUltimoVisto")
    @JsonView(Views.Summary.class)
    public Integer getIdCapituloUltimoVisto() {
        return ultimoVisto == null ? null : ultimoVisto.getIdCapitulo();
    }

    public void registrarVisualizacion(Capitulo capituloVisto, List<Visualizacion> visualizacionesDeLaSerie) {
        Capitulo capituloMasAvanzado = capituloMasAvanzado(visualizacionesDeLaSerie, capituloVisto);
        this.ultimoVisto = capituloMasAvanzado;
        this.estadoSerie = serie != null && serie.esUltimoCapitulo(capituloMasAvanzado)
                ? EstadoSerie.TERMINADA
                : EstadoSerie.EMPEZADA;
    }

    public EstadoSerie estadoEfectivo(int capitulosVistos) {
        if (estadoSerie == EstadoSerie.TERMINADA
                || (serie != null && serie.esUltimoCapitulo(ultimoVisto))) {
            return EstadoSerie.TERMINADA;
        }
        if (capitulosVistos > 0) {
            return EstadoSerie.EMPEZADA;
        }
        return estadoSerie == null ? EstadoSerie.PENDIENTE : estadoSerie;
    }

    public Integer idTemporadaUltimoVistoSiEmpezada() {
        if (estadoSerie != EstadoSerie.EMPEZADA
                || ultimoVisto == null
                || ultimoVisto.getTemporada() == null) {
            return null;
        }
        return ultimoVisto.getTemporada().getIdTemporada();
    }

    private Capitulo capituloMasAvanzado(List<Visualizacion> visualizacionesDeLaSerie, Capitulo capituloFallback) {
        if (visualizacionesDeLaSerie == null || visualizacionesDeLaSerie.isEmpty()) {
            return capituloFallback;
        }

        return visualizacionesDeLaSerie.stream()
                .map(Visualizacion::getCapitulo)
                .filter(capitulo -> capitulo != null && capitulo.perteneceA(serie))
                .max(Capitulo::compararOrdenNarrativo)
                .orElse(capituloFallback);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SeguimientoSerie seguimientoSerie)) {
            return false;
        }
        return idSeguimientoSerie != 0
                && idSeguimientoSerie == seguimientoSerie.idSeguimientoSerie;
    }

    @Override
    public int hashCode() {
        return SeguimientoSerie.class.hashCode();
    }
}
