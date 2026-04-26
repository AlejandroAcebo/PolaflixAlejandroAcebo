package application.model.dto.capitulo;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CapituloRequestDto {
    
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
