package application.model.dto.visualizacion;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisualizacionResponseDto {
    
    @JsonProperty("idVisualizacion")
    private int idVisualizacion;
    
    @JsonProperty("idUsuario")
    private int idUsuario;
    
    @JsonProperty("idCapitulo")
    private int idCapitulo;
    
    @JsonProperty("fechaVisualizacion")
    private String fechaVisualizacion;
}
