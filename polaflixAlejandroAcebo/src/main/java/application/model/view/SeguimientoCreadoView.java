package application.model.view;

public record SeguimientoCreadoView(
        int idSeguimientoSerie,
        int idSerie,
        Integer idCapituloUltimoVisto,
        String estadoSerie) {
}
