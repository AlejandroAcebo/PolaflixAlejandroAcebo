package application.model.entity.serie;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import application.model.entity.persona.Persona;
import application.model.enums.TipoSerie;
import application.model.view.Views;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Serie {

    @Id
    @GeneratedValue
    @JsonView(Views.Summary.class)
    private int idSerie;
    
    @JsonView(Views.Summary.class)
    private String nombreSerie;
    
    @JsonView(Views.Summary.class)
    private String sinopsis;
    
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Summary.class)
    private TipoSerie tipoSerie;
    
    @OneToMany(mappedBy="serie",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Temporada> temporadas;
    
    @ManyToMany
    @JoinTable(name = "creadores_series",
            joinColumns = @JoinColumn(name = "idSerie"),
            inverseJoinColumns = @JoinColumn(name = "idPersona"))
    @JsonView(Views.Detail.class)
    private List<Persona> creadores;
    
    @ManyToMany
    @JoinTable(name = "actores_series",
            joinColumns = @JoinColumn(name = "idSerie"),
            inverseJoinColumns = @JoinColumn(name = "idPersona"))
    @JsonView(Views.Detail.class)
    private List<Persona> actores;

    public static Serie crear(String nombreSerie, String sinopsis, TipoSerie tipoSerie) {
        return Serie.builder()
                .nombreSerie(nombreSerie)
                .sinopsis(sinopsis)
                .tipoSerie(tipoSerie)
                .build();
    }

    public int totalCapitulos() {
        return capitulos().size();
    }

    public boolean esUltimoCapitulo(Capitulo capitulo) {
        return ultimoCapitulo()
                .map(ultimo -> ultimo.equals(capitulo))
                .orElse(false);
    }

    public Optional<Capitulo> ultimoCapitulo() {
        return capitulos().stream()
                .max(Capitulo::compararOrdenNarrativo);
    }

    public double precioPorCapitulo() {
        return tipoSerie == null
                ? TipoSerie.ESTANDAR.getPrecioCapitulo()
                : tipoSerie.getPrecioCapitulo();
    }

    public String inicialCatalogo() {
        if (!tieneTexto(nombreSerie)) {
            return "0-9";
        }
        return inicialNormalizada(nombreSerie);
    }

    public boolean coincideConBusqueda(String busqueda) {
        return tieneTexto(busqueda)
                && nombreSerie != null
                && nombreSerie.toLowerCase().contains(busqueda.trim().toLowerCase());
    }

    public static String normalizarInicialCatalogo(String inicial) {
        if (!tieneTexto(inicial)) {
            return "A";
        }
        return inicialNormalizada(inicial);
    }

    private List<Capitulo> capitulos() {
        if (temporadas == null) {
            return List.of();
        }

        return temporadas.stream()
                .filter(Objects::nonNull)
                .map(Temporada::getCapitulos)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .toList();
    }

    private static String inicialNormalizada(String value) {
        String first = value.trim().substring(0, 1).toUpperCase();
        return first.matches("[A-Z]") ? first : "0-9";
    }

    private static boolean tieneTexto(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Serie serie)) {
            return false;
        }
        return idSerie != 0 && idSerie == serie.idSerie;
    }

    @Override
    public int hashCode() {
        return Serie.class.hashCode();
    }
}
