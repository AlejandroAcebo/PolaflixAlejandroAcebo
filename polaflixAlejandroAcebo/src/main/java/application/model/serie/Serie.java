package application.model.serie;

import java.util.List;

import application.model.enums.TipoSerie;
import application.model.persona.Persona;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    
    @OneToMany(mappedBy="serie",cascade = CascadeType.ALL)
    private List<Temporada> temporadas;
    
    @ManyToMany
    @JoinTable(name = "creadores_series",
            joinColumns = @JoinColumn(name = "idSerie"),
            inverseJoinColumns = @JoinColumn(name = "idPersona"))
    private List<Persona> creadores;
    
    @ManyToMany
    @JoinTable(name = "actores_series",
            joinColumns = @JoinColumn(name = "idSerie"),
            inverseJoinColumns = @JoinColumn(name = "idPersona"))
    private List<Persona> actores;
}
