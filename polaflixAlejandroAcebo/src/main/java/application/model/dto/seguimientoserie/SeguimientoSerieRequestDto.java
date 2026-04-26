package application.model.dto.seguimientoserie;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeguimientoSerieRequestDto {
    
    @JsonProperty("idSerie")
    private int idSerie;
}
