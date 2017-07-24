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
 * @author gian
 */
public class SocketConexionSecundario {
    
    public String descargarVid(String ipServidorCentral, int puertoServidor, String nombreVid) throws IOException{
         BufferedReader entrada = null;
         PrintWriter salida = null;
         Socket s = null;
         
        //Inicializo la conexion con el socket
         try{
            //s = new Socket("192.168.0.2", 500);
            s = new Socket(ipServidorCentral, puertoServidor);
            System.out.println("Se inicializa el socket:" + s);
            entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Obtenemos el canal de salida
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
         }catch(IOException e){
             System.out.println(e.getMessage());
         }    
         
                 BufferedReader stdIn =	new BufferedReader(new InputStreamReader(System.in));
        String linea=null;
        
        try {
          while (true) {
            // armo el string para pasar al servidor
            //String str = "inscribir"+direccionIp;
            String str = "descarga_"+nombreVid;
            // La envia al servidor
            salida.println(str);
            System.out.println("Se envio: "+ str);
            // Envía a la salida estándar la respuesta del servidor
            linea = entrada.readLine();
            System.out.println("Respuesta servidor: " + linea);    
            break;
          }
        } catch (Exception e) {
            return "Cliente > Problemas de conexión con el servidor, intente más tarde";
        }
         
         
        // Libera recursos
        salida.close();
        entrada.close();
        stdIn.close();
        s.close();
        return linea;    
    }
}
