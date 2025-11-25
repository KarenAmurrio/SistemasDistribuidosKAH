/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarea2;

/**
 *
 * @author manjo
 */
public class Raya {
    private final char[][] tablero;
    private char jugadorActual;
    private boolean juegoTerminado;
    private char ganador;

    public Raya() {
        tablero = new char[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tablero[i][j] = ' ';
        jugadorActual = 'X'; 
    }

    public char getJugadorActual() {
        return jugadorActual;
    }

    public boolean jugar(int fila, int columna) {
        if (fila < 0 || fila > 2 || columna < 0 || columna > 2 || tablero[fila][columna] != ' ') {
            return false; // movimiento inválido
        }
        tablero[fila][columna] = jugadorActual;
        return true;
    }

    public boolean hayGanador() {
        // Revisar filas y columnas
        for (int i = 0; i < 3; i++) {
            if ((tablero[i][0] == jugadorActual && tablero[i][1] == jugadorActual && tablero[i][2] == jugadorActual) ||
                (tablero[0][i] == jugadorActual && tablero[1][i] == jugadorActual && tablero[2][i] == jugadorActual)) {
                return true;
            }
        }
        // Revisar diagonales
        if ((tablero[0][0] == jugadorActual && tablero[1][1] == jugadorActual && tablero[2][2] == jugadorActual) ||
            (tablero[0][2] == jugadorActual && tablero[1][1] == jugadorActual && tablero[2][0] == jugadorActual)) {
            return true;
        }
        return false;
    }

    public boolean hayEmpate() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (tablero[i][j] == ' ')
                    return false;
        return true;
        
    }

    public void cambiarJugador() {
        jugadorActual = (jugadorActual == 'X') ? 'O' : 'X';
    }

    public void mostrarTablero() {
        System.out.println("  0 1 2");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(tablero[i][j]);
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----");
        }
    }

    public char[][] getTablero() {
        return tablero;
    }
}
