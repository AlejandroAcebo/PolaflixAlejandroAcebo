package application.model.usuario;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PlanPorDemanda extends Plan{

    @Override
    public double calcularCoste() {
        
        return 0;
    }

}
