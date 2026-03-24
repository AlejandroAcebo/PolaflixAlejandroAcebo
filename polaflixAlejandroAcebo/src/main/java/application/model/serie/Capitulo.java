package application.model.serie;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Capitulo {

    @Id
    @GeneratedValue
    private int idCapitulo;
    
    private String nombreCapitulo;
    
    private int numeroCapitulo;
    
    private String enlace;
    
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "idTemporada", nullable = false)
    private Temporada temporada;
    
    
}
