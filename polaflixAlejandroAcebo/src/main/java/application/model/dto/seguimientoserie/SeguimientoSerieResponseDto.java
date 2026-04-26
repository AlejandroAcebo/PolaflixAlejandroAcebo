package application.model.dto.seguimientoserie;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeguimientoSerieResponseDto {
    
    @JsonProperty("idSeguimientoSerie")
    private int idSeguimientoSerie;
    
    @JsonProperty("idSerie")
    private int idSerie;
    
    @JsonProperty("idCapituloUltimoVisto")
    private Integer idCapituloUltimoVisto;
    
    @JsonProperty("estadoSerie")
    private String estadoSerie;
}
