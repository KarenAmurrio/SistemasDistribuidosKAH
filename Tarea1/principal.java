import java.util.Scanner;

public class principal {
    static int valor = 0;

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        int opcion;

        do {
            mostrarMenu();
            opcion = entrada.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese un número entero: ");
                    valor = entrada.nextInt();
                    break;
                case 2:
                    System.out.println("Fibonacci de " + valor + " es: " + calcularFibonacci(valor));
                    break;
                case 3:
                    System.out.println("Factorial de " + valor + " es: " + calcularFactorial(valor));
                    break;
                case 4:
                    System.out.println("Sumatoria hasta " + valor + " es: " + calcularSumatoria(valor));
                    break;
                case 0:
                    System.out.println("Finalizando el programa.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (opcion != 0);

        entrada.close();
    }

    public static void mostrarMenu() {
        System.out.println("\n----- MENÚ DE OPCIONES -----");
        System.out.println("1. Ingresar número");
        System.out.println("2. Calcular Fibonacci");
        System.out.println("3. Calcular Factorial");
        System.out.println("4. Calcular Sumatoria");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    public static int calcularFibonacci(int n) {
        if (n <= 1) return n;
        return calcularFibonacci(n - 1) + calcularFibonacci(n - 2);
    }

    public static int calcularFactorial(int n) {
        return (n == 0) ? 1 : n * calcularFactorial(n - 1);
    }

    public static int calcularSumatoria(int n) {
        return (n * (n + 1)) / 2;
    }
}
