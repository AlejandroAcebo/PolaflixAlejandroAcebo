package application.model.usuario;

import java.util.List;

import application.model.seguimientoserie.Visualizacion;
import application.model.serie.Capitulo;
import application.model.serie.Serie;
import application.model.serie.Temporada;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_plan", discriminatorType = DiscriminatorType.STRING)
public abstract class Plan {

    @Id
    private int idPlan;
    
    protected double precio;
    
    public abstract double calcularCoste();
}
