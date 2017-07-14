/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.central;

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
        ConsolaShakira consola = new ConsolaShakira();
        consola.show();
        BaseDeDatos bdd = new BaseDeDatos();
        bdd.conexion();
        try {
            
            ServerSocket s = new ServerSocket(8888);
            for (;;){
                
                System.out.println(i + "\n");
                Socket ss = s.accept();
                new SocketConexionHilo(ss,i).start();
                i++;
                
            }
        }catch (Exception e){
            
            i=0;
            
        }
        
    }
}