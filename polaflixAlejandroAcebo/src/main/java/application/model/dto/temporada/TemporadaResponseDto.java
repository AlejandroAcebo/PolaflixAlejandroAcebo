package application.model.dto.temporada;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TemporadaResponseDto {
    
    @JsonProperty("idTemporada")
    private int idTemporada;
    
    @JsonProperty("idSerie")
    private int idSerie;
    
    @JsonProperty("nombreTemporada")
    private String nombreTemporada;
    
    @JsonProperty("numeroTemporada")
    private int numeroTemporada;
}
