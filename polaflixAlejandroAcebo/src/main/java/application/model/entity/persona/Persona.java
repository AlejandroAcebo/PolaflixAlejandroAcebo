package application.model.entity.persona;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import application.model.view.Views;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Persona {

    @Id
    @GeneratedValue
    @JsonView(Views.Summary.class)
    private int idPersona;
    
    @JsonView(Views.Summary.class)
    private String nombrePersona;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Persona persona)) {
            return false;
        }
        return idPersona != 0 && idPersona == persona.idPersona;
    }

    @Override
    public int hashCode() {
        return Persona.class.hashCode();
    }
}
