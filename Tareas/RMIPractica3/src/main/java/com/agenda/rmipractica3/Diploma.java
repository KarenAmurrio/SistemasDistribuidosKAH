/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.agenda.rmipractica3;

/**
 *
 * @author manjo
 */
import java.io.Serializable;

public class Diploma implements Serializable {
    public String nombreCompleto;
    public String carrera;
    public String fecha; // Fecha de emisión
    public String mensaje; // Mensaje de error o vacío

    public Diploma(String nombreCompleto, String carrera, String fecha, String mensaje) {
        this.nombreCompleto = nombreCompleto;
        this.carrera = carrera;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Diploma{" +
               "nombreCompleto='" + nombreCompleto + '\'' +
               ", carrera='" + carrera + '\'' +
               ", fecha='" + fecha + '\'' +
               ", mensaje='" + mensaje + '\'' +
               '}';
    }
}