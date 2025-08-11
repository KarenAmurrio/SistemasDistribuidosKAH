package biblioteca;
import java.util.ArrayList;

public class Armario {
    private String codigo;
    private String tipo; // madera o metálico
    private ArrayList<Publicacion> publicaciones = new ArrayList<>();

    public Armario(String codigo, String tipo) {
        this.codigo = codigo;
        this.tipo = tipo;
    }

    public void agregarPublicacion(Publicacion p) {
        publicaciones.add(p);
    }

    public void mostrar() {
        System.out.println("Armario: " + codigo + " (" + tipo + ")");
        for (Publicacion p : publicaciones) {
            p.mostrar();
        }
    }

    public String getCodigo() {
    return this.codigo;
}

}

