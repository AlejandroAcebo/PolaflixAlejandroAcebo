package application.model.facturacion;

import java.time.LocalDate;

import application.model.serie.Serie;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;


@Value
@Embeddable
public class Cargo {
    
    private LocalDate fecha;
    
    private double precio;
    
    private String nombreSerie;
    
    private int idCapitulo;
    
    private int idTemporada;
    

}
