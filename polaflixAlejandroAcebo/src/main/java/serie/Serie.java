package serie;

import java.util.List;

import enums.TipoSerie;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persona.Persona;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Serie {

    @Id
    @GeneratedValue
    private int idSerie;
    
    private String nombreSerie;
    
    private String sinopsis;
    
    private TipoSerie tipoSerie;
    
    private List<Temporada> temporadas;
    
    private List<Persona> creadores;
    
    private List<Persona> actores;
}
