package application.model.entity.usuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("FIJO")
public class PlanFijo extends Plan{

    @Override
    public double calcularCoste() {
        return getPrecio();
    }

    @Override
    public boolean esCuotaFija() {
        return true;
    }

}
