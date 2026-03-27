package application.model.usuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue("DEMANDA")
public class PlanPorDemanda extends Plan{

    @Override
    public double calcularCoste() {
        
        return 0;
    }

}
