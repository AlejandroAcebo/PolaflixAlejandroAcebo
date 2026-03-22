package application.model.usuario;

import java.util.List;

import application.model.facturacion.Factura;
import application.model.serie.Serie;

import static java.util.Objects.nonNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
public class Usuario {

    @Id
    @GeneratedValue
    private int idUsuario;
    
    private String nombre;
    
    private String password;
    
    private String cuentaBancaria;
    
    private boolean cuotaFija;
    
    @OneToMany
    @JoinColumn(name = "idUsuario")
    private List<Factura> facturas;
    
    @ManyToMany
    @JoinTable(name = "usuario_series_terminadas")
    private List<Serie> seriesTerminadas;
    
    @ManyToMany
    @JoinTable(name = "usuario_series_pendientes")
    private List<Serie> seriesPendientes;
    
    @ManyToMany
    @JoinTable(name = "usuario_series_empezadas")
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
