/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaServidor;

/**
 *
 * @author Alejandro Fernandez
 */
public class ConsolaServidor extends Thread{
    
    @Override
    public void run(){
        System.out.println("Soy el hilo del cliente secundario");
    }
}
