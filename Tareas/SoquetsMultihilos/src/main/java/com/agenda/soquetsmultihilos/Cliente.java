/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.agenda.soquetsmultihilos;

/**
 *
 * @author manjo
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Cliente {
    private static final String HOST = "localhost"; // Cambiar si el servidor está en otra máquina
    private static final int PUERTO = 12345;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(HOST, PUERTO);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conectado al servidor de preguntas.");
            
            // Thread para recibir mensajes del servidor de forma continua
            Thread receptor = new Thread(new ReceptorServidor(in));
            receptor.start();
            
            // Lógica principal de envío (solo para el nombre del jugador y respuestas)
            
            // 1. Enviar Nombre (la primera interacción con el servidor)
            // El ReceptorServidor ya mostró el mensaje "BIENVENIDO: Ingrese su nombre:"
            System.out.print("Su nombre: ");
            String nombre = scanner.nextLine();
            out.writeObject(nombre);
            out.flush();
            
            // 2. Bucle de envío de respuestas
            while (receptor.isAlive()) {
                // El cliente debe esperar a que el servidor le pida una respuesta
                System.out.print("\nIngrese el número de su respuesta (1, 2, 3...): ");
                String respuesta = scanner.nextLine();
                
                // Si la respuesta es un número válido, la enviamos.
                if (respuesta.matches("\\d+")) {
                    out.writeObject(respuesta);
                    out.flush();
                } else {
                    System.out.println("Entrada inválida. Debe ser un número.");
                }
            }
            
            receptor.join(); // Esperar a que el thread de recepción termine.

        } catch (UnknownHostException e) {
            System.err.println("Host no encontrado: " + HOST);
        } catch (IOException e) {
            System.err.println("No se pudo conectar al servidor: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Clase que se ejecuta en un thread separado para escuchar mensajes del servidor
class ReceptorServidor implements Runnable {
    private ObjectInputStream in;

    public ReceptorServidor(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object objetoRecibido = in.readObject();
                
                if (objetoRecibido instanceof Pregunta) {
                    // Recibimos una nueva pregunta
                    Pregunta p = (Pregunta) objetoRecibido;
                    System.out.println("\n--- NUEVA PREGUNTA ---");
                    System.out.println(p);
                    System.out.print("Ingrese el número de su respuesta (1, 2, 3...): ");
                } else if (objetoRecibido instanceof String) {
                    // Recibimos un mensaje de estado (BIENVENIDO, RESULTADO, MARCADOR, FIN)
                    String mensaje = (String) objetoRecibido;
                    
                    if (mensaje.startsWith("MARCADOR:")) {
                        System.out.println("\n" + mensaje.substring(9)); // Mostrar el marcador
                    } else if (mensaje.startsWith("FIN:")) {
                        System.out.println("\n" + mensaje);
                        break; // Terminar el thread de recepción
                    } else if (mensaje.startsWith("BIENVENIDO:") || mensaje.startsWith("RESULTADO:")) {
                        System.out.println(mensaje);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Conexión con el servidor cerrada o perdida.");
        }
    }
}
