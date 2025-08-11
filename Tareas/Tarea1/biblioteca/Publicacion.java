package biblioteca;

public abstract class Publicacion {
    protected String nombre;

    public Publicacion(String nombre) {
        this.nombre = nombre;
    }

    public abstract void mostrar();
}
