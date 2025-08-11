package biblioteca;
import java.util.List;

public class Periodico extends Publicacion {
    private String fecha;
    private List<String> suplementos;

    public Periodico(String nombre, String fecha, List<String> suplementos) {
        super(nombre);
        this.fecha = fecha;
        this.suplementos = suplementos;
    }

    public void mostrar() {
        System.out.println("Periódico: " + nombre + ", Fecha: " + fecha + ", Suplementos: " + suplementos);
    }
}
