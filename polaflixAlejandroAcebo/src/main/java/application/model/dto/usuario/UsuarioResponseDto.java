package application.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsuarioResponseDto {

    @JsonProperty("idUsuario")
    private int idUsuario;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("cuentaBancaria")
    private String cuentaBancaria;

    @JsonProperty("idPlan")
    private int idPlan;
}
