package application.model.entity.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_plan", discriminatorType = DiscriminatorType.STRING)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class Plan {

    @Id
    private int idPlan;
    
    protected double precio;
    
    public abstract double calcularCoste();

    public abstract boolean esCuotaFija();

    @JsonProperty("tipoPlan")
    public String getTipoPlan() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Plan plan)) {
            return false;
        }
        return idPlan != 0 && idPlan == plan.idPlan;
    }

    @Override
    public int hashCode() {
        return Plan.class.hashCode();
    }
}
