package application.model.view;

public record CapituloDetalleView(
        int idCapitulo,
        int idTemporada,
        String nombreCapitulo,
        int numeroCapitulo,
        String enlace,
        String descripcion,
        boolean visto) {
}
