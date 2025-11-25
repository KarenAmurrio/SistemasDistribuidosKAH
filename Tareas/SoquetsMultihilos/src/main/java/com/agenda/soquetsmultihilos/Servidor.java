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
import java.util.*;

public class Servidor {
    private static final int PUERTO = 12345;
    private List<Pregunta> bancoPreguntas;
    private Map<String, Integer> marcador; // Nombre del jugador -> Puntuación
    private List<ManejadorCliente> clientesConectados; // Para enviar actualizaciones a todos

    public Servidor() {
        bancoPreguntas = cargarPreguntas();
        marcador = new HashMap<>();
        clientesConectados = new ArrayList<>();
    }

    private List<Pregunta> cargarPreguntas() {
        // Simulación de un banco de preguntas
        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(new Pregunta(
            "¿Cuál es la capital de Francia?",
            new String[]{"Berlín", "Madrid", "París", "Roma"},
            2 // París
        ));
        preguntas.add(new Pregunta(
            "¿Qué planeta es conocido como el 'Planeta Rojo'?",
            new String[]{"Júpiter", "Marte", "Venus", "Tierra"},
            1 // Marte
        ));
        // Añadir más preguntas...
        return preguntas;
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado. Escuchando en el puerto " + PUERTO);

            while (true) {
                // Esperar y aceptar una nueva conexión de cliente
                Socket socketCliente = serverSocket.accept();
                System.out.println("Nuevo jugador conectado: " + socketCliente);
                
                // Crear un manejador de cliente (thread) para esta conexión
                ManejadorCliente manejador = new ManejadorCliente(socketCliente, this);
                clientesConectados.add(manejador);
                
                // Iniciar el thread
                new Thread(manejador).start();
            }
        } catch (IOException e) {
            System.err.println("Error del servidor: " + e.getMessage());
        }
    }

    // Método llamado por los clientes para actualizar el marcador
    public synchronized void actualizarPuntuacion(String nombreJugador, int puntos) {
        marcador.put(nombreJugador, marcador.getOrDefault(nombreJugador, 0) + puntos);
        System.out.println("Puntuación de " + nombreJugador + " actualizada a " + marcador.get(nombreJugador));
        
        // Notificar a todos los clientes sobre el marcador actualizado
        notificarMarcador();
    }
    
    // Eliminar un cliente de la lista cuando se desconecta
    public synchronized void removerCliente(ManejadorCliente cliente, String nombreJugador) {
        clientesConectados.remove(cliente);
        System.out.println("Jugador desconectado: " + nombreJugador);
    }
    
    // Envía el marcador actual a todos los clientes
    private void notificarMarcador() {
        String marcadorStr = construirMarcador();
        for (ManejadorCliente cliente : clientesConectados) {
            cliente.enviarMensaje("MARCADOR:" + marcadorStr);
        }
    }
    
    // Construye la cadena del marcador (para simplificar la comunicación)
    private String construirMarcador() {
        StringBuilder sb = new StringBuilder("Marcador Actual:\n");
        // Ordenar por puntuación para el ranking
        marcador.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" puntos\n"));
        return sb.toString();
    }
    
    // Método principal para iniciar el servidor
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciar();
    }
}
