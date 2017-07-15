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
import java.util.Scanner;

/**
 *
 * @author Alejandro Fernandez
 */
public class SocketConexion {
    
    /**
     * Probando Hilo
     */
    public void conexionPrueba() {
        int c;
        try{
            Socket s=new Socket("192.168.0.2",500);
            InputStream in=s.getInputStream();
            OutputStream out=s.getOutputStream();
            String str="Hola menor";
            byte buf[]=str.getBytes();
            out.write(buf);
            s.close();
            
        }catch(Exception e) {
            
            System.out.println(e);
            
        }
    }
    
    
    public void inscribirUsuario(String direccionIp, int puerto) throws IOException{
         BufferedReader entrada = null;
         PrintWriter salida = null;
         Socket s = null;
         
         //Inicializo la conexion con el socket
         try{
            s = new Socket("192.168.0.2", 500);
            entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Obtenemos el canal de salida
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())),true);
         }catch(IOException e){
             System.out.println(e.getMessage());
         }
         
        BufferedReader stdIn =	new BufferedReader(new InputStreamReader(System.in));
        String linea;
        
        try {
          while (true) {
            // Leo la entrada del usuario
            InetAddress adress = InetAddress.getLocalHost();
            String str = adress.getHostAddress();
            // La envia al servidor
            salida.println(str);
            // Envía a la salida estándar la respuesta del servidor
            linea = entrada.readLine();
            System.out.println("Respuesta servidor: " + linea);
            
            break;
          }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }

        // Libera recursos
        salida.close();
        entrada.close();
        stdIn.close();
        s.close();
  }
        /*try{
            int c;
            Socket s=new Socket("192.168.0.1",500);
            DataOutputStream mensaje;
            BufferedReader entrada = new BufferedReader ( new InputStreamReader(s.getInputStream()));
            //Verificando conexion
            System.out.println(entrada.readLine());
            mensaje = new DataOutputStream(s.getOutputStream());
 + " f";
            mensaje.writeUTF(str);
            System.out.println(entrada.readLine());
            System.out.println(entrada.readLine());
            s.close();
            System.out.println("cerrar socket");
            }catch(IOException e){
                
                System.out.println(e);
                
            }*/
    }
