package application.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioRequestDto {

    @NotBlank
    @JsonProperty("nombre")
    private String nombre;

    @NotBlank
    @JsonProperty("password")
    private String password;

    @NotBlank
    @JsonProperty("cuentaBancaria")
    private String cuentaBancaria;

    @NotNull
    @JsonProperty("idPlan")
    private Integer idPlan;
}
