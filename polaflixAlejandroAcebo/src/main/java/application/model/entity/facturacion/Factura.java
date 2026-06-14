package application.model.entity.facturacion;

import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import application.model.entity.usuario.Usuario;
import application.model.view.Views;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Factura {

    @Id
    @GeneratedValue
    @JsonView(Views.Summary.class)
    private int idFactura;
    
    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private LocalDate fecha;
    
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @JsonView(Views.Summary.class)
    public double getTotal() {
        if (usuario != null && usuario.tieneCuotaFija()) {
            return usuario.getPlan().calcularCoste();
        }
        return cargos == null ? 0 : cargos.stream()
                .mapToDouble(Cargo::getPrecio)
                .sum();
    }

    @JsonProperty("idUsuario")
    @JsonView(Views.Summary.class)
    public int getIdUsuario() {
        return usuario == null ? 0 : usuario.getIdUsuario();
    }
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cargo", joinColumns = @JoinColumn(name = "idFactura"))
    @JsonView(Views.Detail.class)
    private List<Cargo> cargos;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Factura factura)) {
            return false;
        }
        return idFactura != 0 && idFactura == factura.idFactura;
    }

    @Override
    public int hashCode() {
        return Factura.class.hashCode();
    }
}
