package application.model.facturacion;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;


@Value
@Embeddable
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Cargo {
    
    private LocalDate fecha;
    
    private double precio;
    
    private String nombreSerie;
    
    private int idCapitulo;
    
    private int idTemporada;
    

}
