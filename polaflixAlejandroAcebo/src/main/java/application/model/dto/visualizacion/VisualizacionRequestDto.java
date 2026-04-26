package application.model.dto.visualizacion;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisualizacionRequestDto {
    
    @JsonProperty("idCapitulo")
    private int idCapitulo;
}
