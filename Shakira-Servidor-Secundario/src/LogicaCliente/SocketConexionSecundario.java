/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaCliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Alejandro Fernandez
 */
public class SocketConexionSecundario {

    
    public String inscribirServidor(String[] puertos) {
        BufferedReader entrada = null;
        PrintWriter salida = null;
        Socket s = null;
        BufferedReader stdIn =	new BufferedReader(new InputStreamReader(System.in));
        String linea=null;
        
         //Inicializo la conexion con el socket
         try{
            s = new Socket("192.168.0.2", 500);
            System.out.println("Se inicializa el socket:" + s);
            entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Obtenemos el canal de salida
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
         }catch(IOException e){
             System.out.println(e.getMessage());
         }
               
        try {
            
          while (true) {
            // Leo la direccion ip del servidor secundario
            String str = "inscribirS_" + s.getLocalAddress() + "_" + puertos[0] + "_" + puertos[1];
            // La envia al servidor
            salida.println(str);
            System.out.println("Se envio: "+ str);
            // Recibe la respuesta del servidor
            linea = entrada.readLine();
            System.out.println("Respuesta servidor: " + linea);    
            break;
            
          }
          
           // Libera recursos
           salida.close();
           entrada.close();
           stdIn.close();
           s.close();
           
        } catch (IOException e) {
            
            return ("Servidor Secundario > Problemas de conexión con el servidor, intente más tarde"); 
            
        } catch (Exception e){
            
            return ("Servidor Secundario > Ha sucedido un error inesperado, intente nuevamente");
            
        }
        
        return linea;
        
    }
    
}
