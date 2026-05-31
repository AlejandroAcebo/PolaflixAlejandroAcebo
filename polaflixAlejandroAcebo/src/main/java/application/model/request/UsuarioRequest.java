package application.model.request;

import java.util.List;

import application.model.enums.CargoRol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioRequest {

    @NotBlank
    private String nombre;

    @NotBlank
    private String contrasena;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{11,30}$", message = "Debe tener formato IBAN")
    private String cuentaBancaria;

    @NotNull
    @Positive
    private Integer idPlan;

    private List<CargoRol> cargos;
}
