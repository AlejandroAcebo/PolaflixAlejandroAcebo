package application.model.view;

import java.util.List;

public record UsuarioResumenView(
        int idUsuario,
        String nombre,
        int idPlan,
        List<String> cargos) {
}
