/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.agenda.soquetsmultihilos;

/**
 *
 * @author manjo
 */
class ManejadorCliente implements Runnable {
    private Socket socket;
    private Servidor servidor;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String nombreJugador;

    public ManejadorCliente(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    // Método para que el servidor pueda enviar mensajes
    public void enviarMensaje(String mensaje) {
        try {
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException e) {
            // Manejar error de envío (ej. cliente desconectado)
            System.err.println("Error al enviar mensaje a " + nombreJugador + ": " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // 1. Inicializar flujos de entrada/salida (importante en este orden)
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // 2. Obtener el nombre del jugador (primer mensaje del cliente)
            enviarMensaje("BIENVENIDO: Ingrese su nombre:");
            nombreJugador = (String) in.readObject();
            servidor.actualizarPuntuacion(nombreJugador, 0); // Inicializar puntuación

            // 3. Lógica del juego: Ciclo de Preguntas
            Random random = new Random();
            List<Pregunta> preguntas = servidor.bancoPreguntas;

            for (int i = 0; i < 3; i++) { // Por ejemplo, 3 rondas
                // Seleccionar una pregunta al azar
                Pregunta p = preguntas.get(random.nextInt(preguntas.size()));
                
                // Enviar la pregunta al cliente
                out.writeObject(p);
                out.flush();

                // Esperar la respuesta del cliente (índice 1-based)
                String respuestaStr = (String) in.readObject();
                int respuestaCliente = Integer.parseInt(respuestaStr);
                
                // Verificar la respuesta
                if (respuestaCliente - 1 == p.getRespuestaCorrecta()) {
                    servidor.actualizarPuntuacion(nombreJugador, 10); // 10 puntos por respuesta correcta
                    enviarMensaje("RESULTADO: Correcto! +10 puntos.");
                } else {
                    enviarMensaje("RESULTADO: Incorrecto. La respuesta era " + (p.getRespuestaCorrecta() + 1));
                }
                
                // Dar un pequeño tiempo para leer el resultado antes de la siguiente pregunta
                Thread.sleep(2000); 
            }
            
            // 4. Fin del juego para este cliente
            enviarMensaje("FIN: El juego ha terminado.");

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            System.err.println("Conexión con " + nombreJugador + " perdida.");
        } finally {
            // 5. Limpieza y desconexión
            servidor.removerCliente(this, nombreJugador);
            try {
                if (socket != null) socket.close();
            } catch (IOException e) { /* Ignorar */ }
        }
    }
}
