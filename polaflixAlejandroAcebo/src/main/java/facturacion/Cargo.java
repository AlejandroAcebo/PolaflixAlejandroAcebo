package facturacion;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;


@Value
@Embeddable
public class Cargo {
    
    private LocalDate fecha;
    
    private double precio;
    
    private int idCapitulo;
    
    private int idTemporada;

}
