package application.model.enums;

public enum TipoSerie {
    ESTANDAR(0.5),
    SILVER(0.75),
    GOLD(1.5);

    private final double precioCapitulo;

    TipoSerie(double precioCapitulo) {
        this.precioCapitulo = precioCapitulo;
    }

    public double getPrecioCapitulo() {
        return precioCapitulo;
    }
}
