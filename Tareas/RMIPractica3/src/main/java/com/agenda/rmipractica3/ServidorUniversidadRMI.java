/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.agenda.rmipractica3;

/**
 *
 * @author manjo
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServidorUniversidadRMI extends Remote {
    [cite_start]// [cite: 9, 18]
    Diploma EmitirDiploma(String CI, String Nombres, String lerApellido, 
                          String 2doApellido, String fecha_nacimiento, String Carrera) throws RemoteException;
}