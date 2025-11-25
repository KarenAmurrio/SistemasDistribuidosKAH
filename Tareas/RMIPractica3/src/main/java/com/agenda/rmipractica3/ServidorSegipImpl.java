package com.agenda.rmipractica3;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author manjo
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServidorSegipRMI extends Remote {
    [cite_start]// [cite: 11]
    Respuesta Verificar(String CI, String Nombres, String Apellidos) throws RemoteException;
}