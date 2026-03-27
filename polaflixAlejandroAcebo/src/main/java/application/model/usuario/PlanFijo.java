package application.model.usuario;

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
    
    private final static double SUBSCRIPTION_PRICE =  20;
    
    @Override
    public double calcularCoste() {
        return SUBSCRIPTION_PRICE;
    }

}
