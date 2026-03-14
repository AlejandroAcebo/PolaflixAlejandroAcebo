package usuario;

import java.util.List;
import static java.util.Objects.nonNull;

import facturacion.Factura;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import serie.Serie;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue
    private int idUsuario;
    
    private String nombre;
    
    private String password;
    
    private String cuentaBancaria;
    
    private boolean cuotaFija;
    
    private List<Factura> facturas;
    
    private List<Serie> seriesTerminadas;
    
    private List<Serie> seriesPendientes;
    
    private List<Serie> seriesEmpezadas;
    
    public void agregarSeriePendiente(Serie seriePendiente) {
        boolean already =  false;
        if (nonNull(seriePendiente)) {
            for (Serie s: seriesPendientes) {
                if(s.getIdSerie() == seriePendiente.getIdSerie()) {
                    already = true;
                    break;
                    }
                }
        }
        if (!already) {
            this.seriesPendientes.add(seriePendiente);
        }
    }
    
    
}
