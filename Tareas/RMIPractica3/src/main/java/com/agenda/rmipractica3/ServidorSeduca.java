/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.agenda.rmipractica3;

/**
 *
 * @author manjo
 */
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

[cite_start]// [cite: 13, 19, 21, 22, 23]
public class ServidorUniversidadImpl extends UnicastRemoteObject implements ServidorUniversidadRMI {

    // Puerto de SEDUCA (TCP) y SERECI (UDP)
    private static final int PUERTO_SEDUCA = 6000;
    private static final int PUERTO_SERECI = 7000;

    protected ServidorUniversidadImpl() throws RemoteException {
        super();
    }
    
    [cite_start]// Método auxiliar para generar el RUDE [cite: 21]
    private String generarRude(String Nombres, String lerApellido, String 2doApellido, String fecha_nacimiento) {
        // Dos letras del nombre + 2 letras del ler apellido + 2 letras del segundo apellido + fecha nacimiento
        String rude = Nombres.substring(0, 2) + lerApellido.substring(0, 2) + 2doApellido.substring(0, 2) + 
                      fecha_nacimiento.replaceAll("-", "");
        return rude;
    }

    [cite_start]// [cite: 9]
    @Override
    public Diploma EmitirDiploma(String CI, String Nombres, String lerApellido, String 2doApellido, 
                                 String fecha_nacimiento, String Carrera) throws RemoteException {
        
        StringBuilder mensajesError = new StringBuilder();
        boolean todoCorrecto = true;
        String Apellidos = lerApellido + " " + 2doApellido;

        [cite_start]// 1. Llamada a SEGIP (RMI) [cite: 19]
        try {
            // Búsqueda del servicio RMI del SEGIP
            ServidorSegipRMI segip = (ServidorSegipRMI) Naming.lookup("rmi://localhost:1099/SegipService");
            Respuesta respSegip = segip.Verificar(CI, Nombres, Apellidos);

            if (!respSegip.estado) {
                mensajesError.append("SEGIP: ").append(respSegip.mensaje).append(". "); [cite_start]// [cite: 20]
                todoCorrecto = false;
            }
        } catch (Exception e) {
            mensajesError.append("Error RMI con SEGIP. ");
            e.printStackTrace();
        }

        [cite_start]// 2. Llamada a SEDUCA (Socket TCP) [cite: 21]
        if (todoCorrecto) {
            try (Socket socket = new Socket("localhost", PUERTO_SEDUCA);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                [cite_start]// [cite: 21]
                String rude = generarRude(Nombres, lerApellido, 2doApellido, fecha_nacimiento);
                
                // Envío de la solicitud
                out.println("verificar-rude:" + rude); // Simulamos el envío con el RUDE
                
                [cite_start]// Recepción de la respuesta [cite: 21]
                String respuesta = in.readLine(); 
                // Formato: respuesta:mensaje

                if (respuesta != null && !respuesta.startsWith("si:")) {
                    mensajesError.append("SEDUCA: ").append(respuesta.split(":")[1]).append(". ");
                    todoCorrecto = false;
                }
            } catch (IOException e) {
                mensajesError.append("Error Socket TCP con SEDUCA. ");
                e.printStackTrace();
                todoCorrecto = false;
            }
        }

        [cite_start]// 3. Llamada a SERECI (Socket UDP) [cite: 22]
        if (todoCorrecto) {
            try (DatagramSocket socketUDP = new DatagramSocket()) {
                String datos = "Ver-fecha:" + Nombres + "," + Apellidos + "," + fecha_nacimiento; [cite_start]// [cite: 22]
                byte[] bufferEnvio = datos.getBytes();
                InetAddress direccionServidor = InetAddress.getByName("localhost");
                
                // Envío
                DatagramPacket paqueteEnvio = new DatagramPacket(bufferEnvio, bufferEnvio.length, direccionServidor, PUERTO_SERECI);
                socketUDP.send(paqueteEnvio);

                // Recepción
                byte[] bufferRecepcion = new byte[1024];
                DatagramPacket paqueteRecepcion = new DatagramPacket(bufferRecepcion, bufferRecepcion.length);
                socketUDP.setSoTimeout(5000); // Timeout de 5 segundos
                socketUDP.receive(paqueteRecepcion);
                
                String respuesta = new String(paqueteRecepcion.getData(), 0, paqueteRecepcion.getLength());
                [cite_start]// Formato: respuesta:mensaje [cite: 22]

                if (respuesta != null && !respuesta.startsWith("si:")) {
                    mensajesError.append("SERECI: ").append(respuesta.split(":")[1]).append(". "); [cite_start]// [cite: 22]
                    todoCorrecto = false;
                }
            } catch (SocketTimeoutException e) {
                mensajesError.append("Error de Timeout Socket UDP con SERECI. ");
                todoCorrecto = false;
            } catch (IOException e) {
                mensajesError.append("Error Socket UDP con SERECI. ");
                e.printStackTrace();
                todoCorrecto = false;
            }
        }

        [cite_start]// 4. Devolver Resultado [cite: 23]
        if (todoCorrecto) {
            String fechaEmision = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String nombreCompleto = Nombres + " " + lerApellido + " " + 2doApellido;
            [cite_start]// [cite: 23]
            return new Diploma(nombreCompleto, Carrera, fechaEmision, ""); // Mensaje vacío si todo es correcto
        } else {
            [cite_start]// [cite: 23]
            return new Diploma("", "", "", mensajesError.toString().trim()); // Devolver solo los mensajes de error
        }
    }

    public static void main(String[] args) {
        try {
            // Configurar el sistema para usar un puerto específico (opcional)
            java.rmi.registry.LocateRegistry.createRegistry(1100);

            ServidorUniversidadRMI stub = new ServidorUniversidadImpl();
            // Registrar el objeto remoto
            java.rmi.Naming.rebind("rmi://localhost:1100/UniversidadService", stub);
            System.out.println("UNIVERSIDAD Servidor RMI listo.");
        } catch (Exception e) {
            System.err.println("Error en ServidorUniversidad: " + e.toString());
            e.printStackTrace();
        }
    }
}
