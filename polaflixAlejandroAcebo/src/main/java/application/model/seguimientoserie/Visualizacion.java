package application.model.seguimientoserie;
import java.time.LocalDate;

import application.model.enums.EstadoSerie;
import application.model.serie.Capitulo;
import application.model.serie.Serie;
import application.model.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Visualizacion {
    
    private LocalDate fechaVisualizacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCapitulo", nullable = false)
    private Capitulo capitulo;
      

}
