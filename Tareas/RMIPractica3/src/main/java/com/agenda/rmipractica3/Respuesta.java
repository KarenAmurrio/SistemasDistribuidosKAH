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

public class Respuesta implements Serializable {
    public boolean estado;
    public String mensaje;

    public Respuesta(boolean estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }
}