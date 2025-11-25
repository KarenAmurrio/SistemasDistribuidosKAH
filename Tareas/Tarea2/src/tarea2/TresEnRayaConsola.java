/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarea2;

/**
 *
 * @author manjo
 */
import java.util.Scanner;

public class TresEnRayaConsola {
    public static void main(String[] args) {
        Raya juego = new Raya();
        Scanner sc = new Scanner(System.in);

        while (true) {
            juego.mostrarTablero();
            System.out.println("Jugador " + juego.getJugadorActual() + ", ingresa fila y columna (0-2):");
            int fila = sc.nextInt();
            int columna = sc.nextInt();

            if (!juego.jugar(fila, columna)) {
                System.out.println("Movimiento inválido, intenta de nuevo.");
                continue;
            }

            if (juego.hayGanador()) {
                juego.mostrarTablero();
                System.out.println("¡Jugador " + juego.getJugadorActual() + " gana!");
                break;
            }

            if (juego.hayEmpate()) {
                juego.mostrarTablero();
                System.out.println("¡Empate!");
                break;
            }

            juego.cambiarJugador();
        }

        sc.close();
    }
}
