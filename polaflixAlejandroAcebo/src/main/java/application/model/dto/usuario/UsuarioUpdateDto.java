package application.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioUpdateDto {

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("password")
    private String password;

    @JsonProperty("cuentaBancaria")
    private String cuentaBancaria;

    @JsonProperty("idPlan")
    private Integer idPlan;
}
