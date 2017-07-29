/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Logica.SocketConexionHilo;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

/**
 *
 * @author pedro
 */
public class ShakiraServidorCentral {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i=0;
        try {
            //puerto del servidor:
                //1030
            
            //Se crea el socket del servidor en el puerto 1030
            ServerSocket s = new ServerSocket(1030);
            while(true){    
                System.out.print("Servidor Central Escuchando en el puerto " + 1030);
                System.out.println(", i = " + i);
                Socket ss = s.accept();
                new SocketConexionHilo(ss,i).start();
                i++;
            }
        }catch (Exception e){
            System.out.println("Error inicializando el socket en el puerto 1030:");
            System.out.println(e.toString());
            i=0;
            
        }
        
    }
}