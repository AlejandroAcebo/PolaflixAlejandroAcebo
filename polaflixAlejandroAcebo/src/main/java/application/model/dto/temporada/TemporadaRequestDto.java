package application.model.dto.temporada;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TemporadaRequestDto {
    
    @JsonProperty("idSerie")
    private int idSerie;
    
    @JsonProperty("nombreTemporada")
    @NotBlank
    private String nombreTemporada;
    
    @JsonProperty("numeroTemporada")
    private int numeroTemporada;
}
