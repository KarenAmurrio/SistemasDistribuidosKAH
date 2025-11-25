/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.agenda.soquetsmultihilos;

/**
 *
 * @author manjo
 */
import java.io.Serializable;

public class Pregunta implements Serializable {
    private String texto;
    private String[] opciones;
    private int respuestaCorrecta; 

    public Pregunta(String texto, String[] opciones, int respuestaCorrecta) {
        this.texto = texto;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    // Getters para acceder a los datos
    public String getTexto() { return texto; }
    public String[] getOpciones() { return opciones; }
    public int getRespuestaCorrecta() { return respuestaCorrecta; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(texto).append("\n");
        for (int i = 0; i < opciones.length; i++) {
            sb.append("  ").append(i + 1).append(". ").append(opciones[i]).append("\n");
        }
        return sb.toString();
    }
}