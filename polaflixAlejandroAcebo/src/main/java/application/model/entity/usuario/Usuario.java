package application.model.entity.usuario;

import java.util.List;
import java.util.Map;

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
    @JoinColumn(name = "idPlan")
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
    @JsonIgnore
    private List<SeguimientoSerie> series; 
    
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
                .series(new ArrayList<>())
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
        if (series == null) {
            series = new ArrayList<>();
        }
        for (SeguimientoSerie s: series) {
            if(s.getSerie() != null && s.getSerie().getIdSerie() == seriePendiente.getIdSerie()) {
                return s;
            }
        }
        SeguimientoSerie seguimientoSerie = SeguimientoSerie.builder()
                .estadoSerie(EstadoSerie.PENDIENTE)
                .usuario(this)
                .serie(seriePendiente)
                .build();
        this.series.add(seguimientoSerie);
        return seguimientoSerie;
    }
    
    public Visualizacion visualizarCapitulo(Capitulo capitulo, Serie serie) {
        Visualizacion visualizacion = Visualizacion.builder()
                .capitulo(capitulo)
                .fechaVisualizacion(LocalDate.now())
                .usuario(this)
                .build();
        if (visualizaciones == null) {
            visualizaciones = new java.util.LinkedHashMap<>();
        }
        getVisualizaciones().computeIfAbsent(serie, key -> new java.util.ArrayList<>()).add(visualizacion);
        if (visualizacionesPersistidas == null) {
            visualizacionesPersistidas = new java.util.ArrayList<>();
        }
        visualizacionesPersistidas.add(visualizacion);
        return visualizacion;
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
