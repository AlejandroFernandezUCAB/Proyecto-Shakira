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
            //puertos de comandos y de datos respectivamente:
            //cmd:  1026
            //data: 1026
            
            //Se crea el socket de comandos en el puerto 1026
            ServerSocket s = new ServerSocket(1026);
            while(true){    
                System.out.print("Servidor Central Escuchando en el puerto " + 1026);
                System.out.println(", i = " + i);
                Socket ss = s.accept();
                new SocketConexionHilo(ss,i).start();
                i++;
            }
        }catch (Exception e){
            System.out.println("Error inicializando el socket 'ServerSocket s = new ServerSocket(1026);'");
            i=0;
            
        }
        
    }
}