package application.model.view;

import java.util.List;

public record SerieDetalleView(
        SerieView serie,
        SeguimientoResumenView seguimiento,
        List<TemporadaDetalleView> temporadas,
        int vistos,
        int totalCapitulos,
        int temporadaInicial) {
}
