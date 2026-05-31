package application.model.view;

import java.util.List;

public record TemporadaDetalleView(
        int idTemporada,
        int idSerie,
        String nombreTemporada,
        int numeroTemporada,
        List<CapituloDetalleView> capitulos) {
}
