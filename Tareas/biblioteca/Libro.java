package biblioteca;
public class Libro extends Publicacion {
    private String autor, editorial;
    private int anio;

    public Libro(String nombre, String autor, String editorial, int anio) {
        super(nombre);
        this.autor = autor;
        this.editorial = editorial;
        this.anio = anio;
    }

    @Override
    public void mostrar() {
        System.out.println("Libro: " + nombre + ", Autor: " + autor + ", Editorial: " + editorial + ", Año: " + anio);
    }
}
