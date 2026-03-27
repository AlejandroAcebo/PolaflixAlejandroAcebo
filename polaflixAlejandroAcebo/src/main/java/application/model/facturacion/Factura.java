package application.model.facturacion;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import application.model.seguimientoserie.Visualizacion;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Factura {

    @Id
    @GeneratedValue
    private int idFactura;
    
    private LocalDate fecha;

    public double getTotal() {
        return cargos.stream()
        .mapToDouble(Cargo::getPrecio)
        .sum(); 
    }
    
    @ElementCollection
    private List<Cargo> cargos;
    
}
