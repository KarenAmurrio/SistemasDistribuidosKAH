package biblioteca;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Biblioteca biblio = new Biblioteca("Facultad Ciencias", 120.5);

        while (true) {
            System.out.println("\n--- MENÚ BIBLIOTECA ---");
            System.out.println("1. Crear armario");
            System.out.println("2. Crear publicación");
            System.out.println("3. Agregar publicación a armario");
            System.out.println("4. Mostrar biblioteca");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    System.out.print("Código del armario: ");
                    String cod = sc.nextLine();
                    System.out.print("Tipo (madera/metalico): ");
                    String tipo = sc.nextLine();
                    biblio.agregarArmario(new Armario(cod, tipo));
                    break;
                case 2:
                    System.out.println("Tipo de publicación (1=Libro, 2=Revista, 3=Periódico): ");
                    int tipoPub = sc.nextInt(); sc.nextLine();
                    if (tipoPub == 1) {
                        System.out.print("Nombre: "); String nom = sc.nextLine();
                        System.out.print("Autor: "); String autor = sc.nextLine();
                        System.out.print("Editorial: "); String edit = sc.nextLine();
                        System.out.print("Año: "); int anio = sc.nextInt();
                        Publicacion libro = new Libro(nom, autor, edit, anio);
                        System.out.print("Código del armario: "); sc.nextLine(); String codArm = sc.nextLine();
                        Armario a1 = biblio.buscarArmario(codArm);
                        if (a1 != null) a1.agregarPublicacion(libro);
                    } else if (tipoPub == 2) {
                        System.out.print("Nombre: "); String nom = sc.nextLine();
                        System.out.print("Mes: "); String mes = sc.nextLine();
                        System.out.print("Año: "); int anio = sc.nextInt(); sc.nextLine();
                        System.out.print("Tipo: "); String t = sc.nextLine();
                        Publicacion rev = new Revista(nom, mes, anio, t);
                        System.out.print("Código del armario: "); String codArm = sc.nextLine();
                        Armario a2 = biblio.buscarArmario(codArm);
                        if (a2 != null) a2.agregarPublicacion(rev);
                    } else if (tipoPub == 3) {
                        System.out.print("Nombre: "); String nom = sc.nextLine();
                        System.out.print("Fecha: "); String fecha = sc.nextLine();
                        List<String> suplementos = new ArrayList<>();
                        String s;
                        do {
                            System.out.print("Añadir suplemento (enter para salir): ");
                            s = sc.nextLine();
                            if (!s.isEmpty()) suplementos.add(s);
                        } while (!s.isEmpty());
                        Publicacion peri = new Periodico(nom, fecha, suplementos);
                        System.out.print("Código del armario: "); String codArm = sc.nextLine();
                        Armario a3 = biblio.buscarArmario(codArm);
                        if (a3 != null) a3.agregarPublicacion(peri);
                    }
                    break;
                case 3:
                    System.out.println("Primero cree una publicación.");
                    break;
                case 4:
                    biblio.mostrar();
                    break;
                case 0:
                    System.out.println("Gracias.");
                    sc.close();
                    return;
            }
        }
    }
}

