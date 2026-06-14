package application.model.view;

public record VisualizacionRegistradaView(
        Integer idVisualizacion,
        String fechaVisualizacion,
        int idUsuario,
        int idCapitulo,
        int idTemporada,
        int idSerie) {
}
