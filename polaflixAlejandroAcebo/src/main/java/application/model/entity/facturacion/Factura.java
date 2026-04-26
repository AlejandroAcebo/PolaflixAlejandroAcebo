package application.model.entity.facturacion;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import application.model.entity.usuario.Usuario;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public double getTotal() {
        return cargos.stream()
        .mapToDouble(Cargo::getPrecio)
        .sum(); 
    }
    
    @ElementCollection
    private List<Cargo> cargos;
    
}
