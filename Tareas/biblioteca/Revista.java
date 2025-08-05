package biblioteca;

public class Revista extends Publicacion {
    private String mes, tipo;
    private int anio;

    public Revista(String nombre, String mes, int anio, String tipo) {
        super(nombre);
        this.mes = mes;
        this.anio = anio;
        this.tipo = tipo;
    }

    public void mostrar() {
        System.out.println("Revista: " + nombre + ", Mes: " + mes + ", Año: " + anio + ", Tipo: " + tipo);
    }
}


