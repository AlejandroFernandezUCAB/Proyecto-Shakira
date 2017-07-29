/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaServidor;
import LogicaCliente.Controladora;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *Socket para enviar los videos al cliente
 * @author pedro & gian
 */
public class ConsolaServidor extends Thread{
    //synchronized
    //https://stackoverflow.com/questions/20001886/multiple-threads-access-the-same-file
    @Override
    public synchronized void run(){
        System.out.println("Soy el hilo del cliente secundario");
                int i=0;
                int puerto = 0;
        try {
            //puerto
             puerto = Integer.parseInt(Controladora.puertosServSecundario[0]);
            
            //Se crea el socket de comandos en el puerto 1026
            ServerSocket s = new ServerSocket(puerto);
            while(true){    
                System.out.print("Servidor Secundario Escuchando en el puerto " + puerto);
                System.out.println(", i = " + i);
                Socket ss =  s.accept();
                new SocketConexionSecundario(ss,i).start();
                i++;
            }
        }catch (Exception e){
            System.out.println("Error inicializando el socket 'ServerSocket s = new ServerSocket("+puerto+");'");
            i=0;
            
        }
    }
}
