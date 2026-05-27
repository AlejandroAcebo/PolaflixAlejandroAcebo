package application.model.enums;

public enum CargoRol {
    ADMIN("Administrador", "Acceso total al sistema"),
    EDITOR("Editor de contenido", "Puede crear y editar series"),
    MODERADOR("Moderador", "Puede revisar y moderar contenido"),
    PREMIUM("Premium", "Acceso a todas las series"),
    USUARIO_ESTANDAR("Usuario estándar", "Acceso a series básicas"),
    GUEST("Invitado", "Acceso limitado");

    private final String descripcion;
    private final String permisos;

    CargoRol(String descripcion, String permisos) {
        this.descripcion = descripcion;
        this.permisos = permisos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPermisos() {
        return permisos;
    }
}
