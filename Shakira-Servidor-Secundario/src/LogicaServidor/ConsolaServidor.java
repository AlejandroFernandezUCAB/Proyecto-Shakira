/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaServidor;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Alejandro Fernandez
 */
public class ConsolaServidor extends Thread{
    
    @Override
    public void run(){
        System.out.println("Soy el hilo del cliente secundario");
                int i=0;
        try {
            //puertos de comandos y de datos respectivamente:
            //cmd:  1026
            //data: 1026
            
            //Se crea el socket de comandos en el puerto 1026
            ServerSocket s = new ServerSocket(1031);
            while(true){    
                System.out.print("Servidor Secundario Escuchando en el puerto " + 1031);
                System.out.println(", i = " + i);
                Socket ss =  s.accept();
                new SocketConexionSecundario(ss,i).start();
                i++;
            }
        }catch (Exception e){
            System.out.println("Error inicializando el socket 'ServerSocket s = new ServerSocket(1031);'");
            i=0;
            
        }
    }
}
