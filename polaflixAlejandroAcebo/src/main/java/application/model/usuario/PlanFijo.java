package application.model.usuario;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PlanFijo extends Plan{
    
    private final static double SUBSCRIPTION_PRICE =  20;
    
    @Override
    public double calcularCoste() {
        return SUBSCRIPTION_PRICE;
    }

}
