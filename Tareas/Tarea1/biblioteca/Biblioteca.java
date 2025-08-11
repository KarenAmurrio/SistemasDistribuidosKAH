package biblioteca;
import java.util.ArrayList;

public class Biblioteca {
    private String nombre;
    private double tamanio;
    private ArrayList<Armario> armarios = new ArrayList<>();

    public Biblioteca(String nombre, double tamanio) {
        this.nombre = nombre;
        this.tamanio = tamanio;
    }

    public void agregarArmario(Armario a) {
        armarios.add(a);
    }

    public void mostrar() {
        System.out.println("\nBiblioteca: " + nombre + " (" + tamanio + " m2)");
        for (Armario a : armarios) {
            a.mostrar();
        }
    }

    public Armario buscarArmario(String codigo) {
        for (Armario a : armarios) {
            if (a.getCodigo().equals(codigo)) return a; // ✅ usa el getter

        }
        return null;
    }
}


