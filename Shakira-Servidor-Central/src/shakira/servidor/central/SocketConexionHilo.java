/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.central;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro Fernandez
 */
public class SocketConexionHilo extends Thread{
       
    private Socket ss;
    private int counter;
    
    public SocketConexionHilo( Socket i, int c){
        ss = i;
        counter = c;
    }
    
    @Override
    public void run() {
        
        BufferedReader entrada = null;
        PrintWriter salida = null;
        System.out.println("Escuchando: al socket" + ss);
        
        try {
            
          entrada = new BufferedReader(new InputStreamReader(ss.getInputStream()));
          // Establece canal de salida
          salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ss.getOutputStream())),true);
          
          //En este while se recibe la información del cliente y se procede a guardar en la base de datos
          //Además se le responde al cliente 
          while (true) {  
            String str = entrada.readLine();
            BaseDeDatos bdd = new BaseDeDatos();
            bdd.agregarUsuarioBDD( str , 1 );
            salida.println( str );
            break;
          }

        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        }  
        
        try{
            salida.close();
            entrada.close();
            ss.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        
      }
        
//    try {
//      int bytesEntrantes;
//      int tamañoString = 0;
//      InputStream in = ss.getInputStream();
//      OutputStream out = ss.getOutputStream();
//      String bytesEnsamblados = "";
//      while ((bytesEntrantes = in.read())!=-1) {
//          
//            bytesEnsamblados = bytesEnsamblados + (char) bytesEntrantes;
//            
//            //Al final de la cadena hay una f para saber que es el final de la cadena y asi poder "parar" el ciclo
//            if( bytesEnsamblados.contains("f") == true  ){
//                
//                bytesEnsamblados.replace('f', ' ');
//                bytesEnsamblados = bytesEnsamblados.trim();
//                System.out.println( "Ip:" + bytesEntrantes ); 
//                /*BaseDeDatos bdd = new BaseDeDatos();
//                bdd.agregarUsuarioBDD( bytesEnsamblados , 500);
//                System.out.println( bytesEntrantes );*/
//                byte buf[] = "Recibido".getBytes();
//                out.write(buf);                       
//                System.out.println("Chao");
//                ss.close();
//                
//            }
//        }
//            
//    }  catch (Exception e) { 
//        
//        System.out.println(e); 
//        
//    }
    }
