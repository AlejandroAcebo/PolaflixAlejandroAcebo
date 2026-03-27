package application.model.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import application.model.enums.EstadoSerie;
import application.model.facturacion.Factura;
import application.model.seguimientoserie.SeguimientoSerie;
import application.model.seguimientoserie.Visualizacion;
import application.model.serie.Capitulo;
import application.model.serie.Serie;

import static java.util.Objects.nonNull;


import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    
    @OneToMany
    @JoinColumn(name = "idUsuario")
    private List<Factura> facturas;
    
    @OneToMany
    private List<SeguimientoSerie> series; 
    
    @OneToMany
    private List<Visualizacion> visualizaciones;  
    
    // PASAR A SERVICE
//    public void agregarSeriePendiente(Serie seriePendiente) {
//        boolean already =  false;
//        if (nonNull(seriePendiente)) {
//            for (SeguimientoSerie s: series) {
//                if (s.getEstadoSerie().equals(EstadoSerie.PENDIENTE)) {
//                    if(s.getSerie().getIdSerie() == seriePendiente.getIdSerie()) {
//                        already = true;
//                        break;
//                    }
//                }
//            }
//        }
//        if (!already) {
//            SeguimientoSerie seguimientoSerie = SeguimientoSerie.builder()
//                    .estadoSerie(EstadoSerie.PENDIENTE)
//                    .serie(seriePendiente)
//                    .build();
//            this.series.add(seguimientoSerie);
//        }
//    }
    
    // PASAR A SERVICE
//    public void visualizarCapitulo(Capitulo capitulo) {  
//        
//    }
    
    
}
