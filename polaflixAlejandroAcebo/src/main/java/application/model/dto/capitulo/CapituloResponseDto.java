package application.model.dto.capitulo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CapituloResponseDto {
    
    @JsonProperty("idCapitulo")
    private int idCapitulo;
    
    @JsonProperty("idTemporada")
    private int idTemporada;
    
    @JsonProperty("nombreCapitulo")
    private String nombreCapitulo;
    
    @JsonProperty("numeroCapitulo")
    private int numeroCapitulo;
    
    @JsonProperty("enlace")
    private String enlace;
    
    @JsonProperty("descripcion")
    private String descripcion;
}
