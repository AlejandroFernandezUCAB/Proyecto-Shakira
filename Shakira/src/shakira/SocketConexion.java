/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 *
 * @author Alejandro Fernandez
 */
public class SocketConexion {

    /**
     * Probando Hilo
     */
    public void conexionPrueba(String[] puertos) {
        int c;
        try{
            Socket s=new Socket("192.168.0.2",1026);
            InputStream in=s.getInputStream();
            OutputStream out=s.getOutputStream();
//            String str="Hola menor";
//            byte buf[]=str.getBytes();
//            out.write(buf);
            
            String str = "inscribir_"+s.getLocalAddress() + "_" + puertos[0] + "_" + puertos[1];
            byte buf[]=str.getBytes();
            out.write(buf);            
            s.close();
            
        }catch(Exception e) {
            
            System.out.println(e);
            
        }
    }
    
    /**
     * Se procede a realizar la conexion con el servidor central para inscribir al cliente
     * @param direccionIp direccion ip del cliente
     * @param puerto puerto del cliente
     * @return SI está o no registrado correctamente
     * @throws IOException 
     */
    public String inscribirUsuario(String[] puertos) throws IOException{
         BufferedReader entrada = null;
         PrintWriter salida = null;
         Socket s = null;
         
         //Inicializo la conexion con el socket
         try{
            //s = new Socket("192.168.0.2", 500);
            s = new Socket("192.168.0.2", 1026);
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
            // Leo la entrada del usuario
            //String str = "inscribir"+direccionIp;
            String str = "inscribir_"+s.getLocalAddress() + "_" + puertos[0] + "_" + puertos[1];
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