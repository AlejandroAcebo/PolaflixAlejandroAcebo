package application.model.view;

import java.util.List;

public record SeriePersonalView(
        int idSerie,
        String nombreSerie,
        String sinopsis,
        String tipoSerie,
        List<String> creadores,
        List<String> actores,
        SeguimientoResumenView seguimiento,
        int totalCapitulos,
        int capitulosVistos) {
}
