package application.model.dto.serie;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SerieRequestDto {
    
    @JsonProperty("nombreSerie")
    @NotBlank
    private String nombreSerie;
    
    @JsonProperty("sinopsis")
    private String sinopsis;
    
    @JsonProperty("tipoSerie")
    private String tipoSerie;
}
