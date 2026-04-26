package application.model.dto.factura;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FacturaResponseDto {
    
    @JsonProperty("idFactura")
    private int idFactura;
    
    @JsonProperty("idUsuario")
    private int idUsuario;
    
    @JsonProperty("fecha")
    private String fecha;
    
    @JsonProperty("total")
    private double total;
}
