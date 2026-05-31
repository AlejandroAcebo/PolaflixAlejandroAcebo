package application.model.view;

import java.util.List;

public record FacturaMensualView(
        int anio,
        int mes,
        boolean cuotaFija,
        List<CargoFacturaView> cargos,
        double total) {
}
