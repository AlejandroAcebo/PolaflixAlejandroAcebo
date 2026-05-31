package application.model.view;

import java.util.List;

public record UsuarioHomeView(
        UsuarioResumenView usuario,
        List<SeriePersonalView> empezadas,
        List<SeriePersonalView> pendientes,
        List<SeriePersonalView> terminadas) {
}
