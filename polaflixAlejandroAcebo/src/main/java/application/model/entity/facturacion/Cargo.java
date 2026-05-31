package application.model.entity.facturacion;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonView;

import application.model.view.Views;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;


@Value
@Embeddable
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Cargo {
    
    @JsonView(Views.Summary.class)
    private LocalDate fecha;
    
    @JsonView(Views.Summary.class)
    private double precio;
    
    @JsonView(Views.Summary.class)
    private String nombreSerie;
    
    @JsonView(Views.Summary.class)
    private int idCapitulo;
    
    @JsonView(Views.Summary.class)
    private int idTemporada;
    

}
