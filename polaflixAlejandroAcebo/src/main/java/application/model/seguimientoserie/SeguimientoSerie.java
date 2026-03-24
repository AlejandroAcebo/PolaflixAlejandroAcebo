package application.model.seguimientoserie;

import java.util.List;

import application.model.enums.EstadoSerie;
import application.model.serie.Capitulo;
import application.model.serie.Serie;
import application.model.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idUsuario", "idSerie"}) 
})
public class SeguimientoSerie {

    @Id
    private int idSeguimientoSerie;
    
    @ManyToOne
    @JoinColumn(name = "idSerie", nullable = false)
    private Serie serie;
    
    @ManyToOne
    @JoinColumn(name = "idCapitulo", nullable = true)
    private Capitulo ultimoVisto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;
    
    private EstadoSerie estadoSerie;
}
