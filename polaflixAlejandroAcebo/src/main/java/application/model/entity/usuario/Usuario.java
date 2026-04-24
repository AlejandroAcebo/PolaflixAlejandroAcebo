package application.model.entity.usuario;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import static java.util.Objects.nonNull;

import java.time.LocalDate;

import application.model.entity.facturacion.Factura;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.seguimientoserie.Visualizacion;
import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Serie;
import application.model.enums.EstadoSerie;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class Usuario {

    @Id
    @GeneratedValue
    private int idUsuario;
    
    private String nombre;
    
    private String password;
    
    private String cuentaBancaria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPlan")
    private Plan plan;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idUsuario")
    private List<Factura> facturas;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeguimientoSerie> series; 
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visualizacion> visualizacionesPersistidas;
    
    @Transient
    private Map<Serie, List<Visualizacion>> visualizaciones;
    
    public void agregarSeriePendiente(Serie seriePendiente) {
        boolean already =  false;
        if (nonNull(seriePendiente)) {
            for (SeguimientoSerie s: series) {
                if (s.getEstadoSerie().equals(EstadoSerie.PENDIENTE)) {
                    if(s.getSerie().getIdSerie() == seriePendiente.getIdSerie()) {
                        already = true;
                        break;
                    }
                }
            }
        }
        if (!already) {
            SeguimientoSerie seguimientoSerie = SeguimientoSerie.builder()
                    .estadoSerie(EstadoSerie.PENDIENTE)
                    .serie(seriePendiente)
                    .build();
            this.series.add(seguimientoSerie);
        }
    }
    
    @PostLoad
    public void reconstruirMapaVisualizaciones() {
        if (visualizacionesPersistidas == null) {
            visualizacionesPersistidas = new java.util.ArrayList<>();
        }
        visualizaciones = new LinkedHashMap<>();
        for (Visualizacion visualizacion : visualizacionesPersistidas) {
            Serie serie = visualizacion.getCapitulo().getTemporada().getSerie();
            visualizaciones.computeIfAbsent(serie, key -> new java.util.ArrayList<>()).add(visualizacion);
        }
    }
    
    public Map<Serie, List<Visualizacion>> getVisualizaciones() {
        if (visualizaciones == null) {
            reconstruirMapaVisualizaciones();
        }
        return visualizaciones;
    }
    
    public void setVisualizaciones(Map<Serie, List<Visualizacion>> visualizaciones) {
        this.visualizaciones = new LinkedHashMap<>();
        this.visualizacionesPersistidas = new java.util.ArrayList<>();
        if (visualizaciones == null) {
            return;
        }
        visualizaciones.forEach((serie, listaVisualizaciones) -> {
            List<Visualizacion> lista = new java.util.ArrayList<>(listaVisualizaciones);
            this.visualizaciones.put(serie, lista);
            this.visualizacionesPersistidas.addAll(lista);
        });
    }
    
    public void visualizarCapitulo(Capitulo capitulo, Serie serie) { 
        Visualizacion visualizacion = Visualizacion.builder()
                .capitulo(capitulo)
                .fechaVisualizacion(LocalDate.now())
                .usuario(this)
                .build();
        getVisualizaciones().computeIfAbsent(serie, key -> new java.util.ArrayList<>()).add(visualizacion);
        if (visualizacionesPersistidas == null) {
            visualizacionesPersistidas = new java.util.ArrayList<>();
        }
        visualizacionesPersistidas.add(visualizacion);
    }
    
}
