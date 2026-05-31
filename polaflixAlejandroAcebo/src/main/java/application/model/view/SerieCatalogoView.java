package application.model.view;

import java.util.List;

public record SerieCatalogoView(
        int idSerie,
        String nombreSerie,
        String sinopsis,
        String tipoSerie,
        List<String> creadores,
        List<String> actores,
        boolean agregada) {
}
