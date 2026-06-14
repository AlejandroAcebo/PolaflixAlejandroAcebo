package application.model.entity.usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Comparator;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import application.model.entity.facturacion.Factura;
import application.model.entity.seguimientoserie.SeguimientoSerie;
import application.model.entity.seguimientoserie.Visualizacion;
import application.model.entity.serie.Capitulo;
import application.model.entity.serie.Serie;
import application.model.enums.CargoRol;
import application.model.enums.EstadoSerie;
import application.model.view.Views;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKey;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Pattern;
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
public class Usuario {

    @Id
    @GeneratedValue
    @JsonView(Views.Summary.class)
    private int idUsuario;
    
    @Column(nullable = false, unique = true)
    @JsonView(Views.Summary.class)
    private String nombre;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;
    
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{11,30}$", message = "Debe tener formato IBAN")
    @JsonView(Views.Detail.class)
    private String cuentaBancaria;
    
    @ManyToOne
    @JoinColumn(name = "idPlan", nullable = false)
    @JsonIgnore
    private Plan plan;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_cargos", joinColumns = @JoinColumn(name = "id_usuario"))
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Summary.class)
    private List<CargoRol> cargos;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Factura> facturas;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "serie")
    @JsonIgnore
    private Map<Serie, SeguimientoSerie> seguimientos;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Visualizacion> visualizacionesPersistidas;

    @Transient
    @JsonIgnore
    private Map<Serie, List<Visualizacion>> visualizaciones;

    @JsonProperty("idPlan")
    @JsonView(Views.Summary.class)
    public Integer getIdPlan() {
        return plan == null ? null : plan.getIdPlan();
    }

    public static Usuario crear(
            String nombre,
            String contrasena,
            String cuentaBancaria,
            Plan plan,
            List<CargoRol> cargos) {
        return Usuario.builder()
                .nombre(nombre == null ? null : nombre.trim())
                .contrasena(contrasena)
                .cuentaBancaria(normalizarIban(cuentaBancaria))
                .plan(plan)
                .cargos(cargosPorDefectoSiVacio(cargos))
                .facturas(new ArrayList<>())
                .seguimientos(new LinkedHashMap<>())
                .visualizacionesPersistidas(new ArrayList<>())
                .visualizaciones(new LinkedHashMap<>())
                .build();
    }

    public void actualizarPerfil(
            String nombre,
            String cuentaBancaria,
            String contrasena,
            Plan plan,
            List<CargoRol> cargos) {
        if (tieneTexto(nombre)) {
            this.nombre = nombre.trim();
        }
        if (tieneTexto(cuentaBancaria)) {
            this.cuentaBancaria = normalizarIban(cuentaBancaria);
        }
        if (tieneTexto(contrasena)) {
            this.contrasena = contrasena;
        }
        if (plan != null) {
            this.plan = plan;
        }
        if (cargos != null && !cargos.isEmpty()) {
            this.cargos = new ArrayList<>(cargos);
        }
    }
    
    public SeguimientoSerie agregarSeriePendiente(Serie seriePendiente) {
        if (seriePendiente == null) {
            return null;
        }
        if (seguimientos == null) {
            seguimientos = new LinkedHashMap<>();
        }
        SeguimientoSerie seguimientoExistente = seguimientos.get(seriePendiente);
        if (seguimientoExistente != null) {
            return seguimientoExistente;
        }
        SeguimientoSerie seguimientoSerie = SeguimientoSerie.builder()
                .estadoSerie(EstadoSerie.PENDIENTE)
                .usuario(this)
                .serie(seriePendiente)
                .build();
        this.seguimientos.put(seriePendiente, seguimientoSerie);
        return seguimientoSerie;
    }

    public List<SeguimientoSerie> seguimientosRegistrados() {
        if (seguimientos == null) {
            return List.of();
        }
        return seguimientos.values().stream().toList();
    }

    public Optional<SeguimientoSerie> seguimientoDeSerie(int idSerie) {
        return seguimientosRegistrados().stream()
                .filter(seguimiento -> seguimiento.getSerie() != null)
                .filter(seguimiento -> seguimiento.getSerie().getIdSerie() == idSerie)
                .findFirst();
    }

    public boolean tieneSerieEnEspacioPersonal(Serie serie) {
        return serie != null && seguimientoDeSerie(serie.getIdSerie()).isPresent();
    }

    public List<SeguimientoSerie> seguimientosPorEstado(EstadoSerie estadoSerie) {
        return seguimientosRegistrados().stream()
                .filter(seguimiento -> estadoSerie == estadoEfectivoDe(seguimiento))
                .toList();
    }

    public EstadoSerie estadoEfectivoDe(SeguimientoSerie seguimiento) {
        if (seguimiento == null) {
            return EstadoSerie.PENDIENTE;
        }
        return seguimiento.estadoEfectivo(capitulosVistosDe(seguimiento.getSerie()));
    }

    public List<Visualizacion> visualizacionesRegistradas() {
        return visualizacionesPersistidas == null ? List.of() : visualizacionesPersistidas;
    }

    public Optional<Visualizacion> visualizacionDe(Capitulo capitulo) {
        if (capitulo == null) {
            return Optional.empty();
        }

        int idCapitulo = capitulo.getIdCapitulo();
        return visualizacionesRegistradas().stream()
                .filter(visualizacion -> visualizacion.idCapitulo() == idCapitulo)
                .findFirst();
    }

    public List<Visualizacion> visualizacionesDe(Serie serie) {
        if (serie == null) {
            return List.of();
        }

        int idSerie = serie.getIdSerie();
        return visualizacionesRegistradas().stream()
                .filter(visualizacion -> visualizacion.idSerie() == idSerie)
                .toList();
    }

    public Set<Integer> idsCapitulosVistos() {
        return visualizacionesRegistradas().stream()
                .map(Visualizacion::idCapitulo)
                .collect(Collectors.toSet());
    }

    public int capitulosVistosDe(Serie serie) {
        return serie == null
                ? 0
                : (int) visualizacionesDe(serie).stream()
                        .map(Visualizacion::idCapitulo)
                        .distinct()
                        .count();
    }

    public List<Visualizacion> visualizacionesDelMes(int anio, int mes) {
        return visualizacionesRegistradas().stream()
                .filter(visualizacion -> visualizacion.perteneceAlMes(anio, mes))
                .sorted(Comparator.comparing(Visualizacion::getFechaVisualizacion).reversed())
                .toList();
    }
    
    public Visualizacion visualizarCapitulo(Capitulo capitulo, Serie serie) {
        Visualizacion visualizacion = Visualizacion.builder()
                .capitulo(capitulo)
                .fechaVisualizacion(LocalDate.now())
                .usuario(this)
                .build();
        if (visualizaciones == null) {
            visualizaciones = new LinkedHashMap<>();
        }
        visualizaciones.computeIfAbsent(serie, key -> new ArrayList<>()).add(visualizacion);
        if (visualizacionesPersistidas == null) {
            visualizacionesPersistidas = new ArrayList<>();
        }
        visualizacionesPersistidas.add(visualizacion);
        return visualizacion;
    }

    public Optional<Visualizacion> registrarVisualizacion(Serie serie, Capitulo capitulo) {
        Optional<SeguimientoSerie> seguimiento = seguimientoDeSerie(serie == null ? 0 : serie.getIdSerie());
        if (seguimiento.isEmpty()) {
            return Optional.empty();
        }

        Visualizacion visualizacion = visualizacionDe(capitulo)
                .orElseGet(() -> visualizarCapitulo(capitulo, serie));
        seguimiento.get().registrarVisualizacion(capitulo, visualizacionesDe(serie));
        return Optional.of(visualizacion);
    }

    public boolean tieneCuotaFija() {
        return plan != null && plan.esCuotaFija();
    }

    public double calcularTotalFacturaMensual(List<Visualizacion> visualizacionesDelMes) {
        if (tieneCuotaFija()) {
            return plan.calcularCoste();
        }
        if (visualizacionesDelMes == null) {
            return 0;
        }

        return visualizacionesDelMes.stream()
                .mapToDouble(Visualizacion::precio)
                .sum();
    }

    public double calcularTotalFacturaMensual(int anio, int mes) {
        return calcularTotalFacturaMensual(visualizacionesDelMes(anio, mes));
    }

    public static List<CargoRol> cargosPorDefectoSiVacio(List<CargoRol> cargos) {
        if (cargos == null || cargos.isEmpty()) {
            return new ArrayList<>(List.of(CargoRol.USUARIO_ESTANDAR));
        }

        return new ArrayList<>(cargos);
    }

    public static String normalizarIban(String iban) {
        return iban == null ? null : iban.replace(" ", "").toUpperCase();
    }

    private boolean tieneTexto(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Usuario usuario)) {
            return false;
        }
        return idUsuario != 0 && idUsuario == usuario.idUsuario;
    }

    @Override
    public int hashCode() {
        return Usuario.class.hashCode();
    }
    
}
