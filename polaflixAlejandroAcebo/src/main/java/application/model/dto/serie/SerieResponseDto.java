package application.model.dto.serie;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SerieResponseDto {
    
    @JsonProperty("idSerie")
    private int idSerie;
    
    @JsonProperty("nombreSerie")
    private String nombreSerie;
    
    @JsonProperty("sinopsis")
    private String sinopsis;
    
    @JsonProperty("tipoSerie")
    private String tipoSerie;
}
