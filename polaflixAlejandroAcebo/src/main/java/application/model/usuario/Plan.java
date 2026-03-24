package application.model.usuario;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public abstract class Plan {

    private int idPlan;
    
    protected double precio;
    
    public abstract double calcularCoste();
}
