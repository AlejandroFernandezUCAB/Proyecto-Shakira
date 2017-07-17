/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import BaseDeDatos.BaseDeDatos;
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
public class SocketConexionCentral extends Thread{
       
    private Socket ss;
    private int counter;
    
    public SocketConexionCentral( Socket i, int c){
        ss = i;
        counter = c;
    }
    
    @Override
    public void run() {
        
        BufferedReader entrada = null;
        PrintWriter salida = null;
        System.out.println("Escuchando: al socket" + ss);
        int suiche;
        try {
            
          //Establece el canal de entrada
          entrada = new BufferedReader(new InputStreamReader(ss.getInputStream()));
          // Establece canal de salida
          salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ss.getOutputStream())),true);
          
          //En este while se recibe la información del cliente y se procede dependiendo el if
          //Además se le responde al cliente 
          while (true) {  
            String str = entrada.readLine();
            
            //En este if se verifica si contiene la cadena "inscribir", esto 
            //significa que va a guardar el cliente en la base de datos
            
            if( str.contains("inscribir usuario")){
                
                suiche = inscribirUsuario(str);                                
                if( suiche == 1){
                   salida.println( "Servidor Central > Inscrito cliente correctamente");                   
                }else{
                   salida.println( "Servidor Central > Ya ud se ha registrado");
                }
                
            }
            
            if( str.trim().contains("inscribir servidor")){
                
                suiche = inscribirServidorSecundario(str);                                
                if( suiche == 1){
                    
                   salida.println( "Servidor Central > Servidor inscrito correctamente");                   
                   
                }else if (suiche == 0){
                    
                   salida.println( "Servidor Central > Ya ud se ha registrado");
                   
                }else{
                   salida.println( "Servidor Central > Ya hay 3 servidores registrados");
                }
                
            }
            
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

    /**
     * Metodo encargado de inscribir el usuario en la base de datos, el metodo quita la paabra inscribir
     * @param entrada 
     * @return si devuelve 1 significa que se guardo correctamente, si devuelve 0 ya estaba registrado
     */
    private int inscribirUsuario(String entrada) {
        
            entrada = entrada.substring(18);
            BaseDeDatos bdd = new BaseDeDatos();
            return bdd.agregarUsuarioBDD(entrada, 1);
            
    }
    
    /**
     * Metodo que inscribe al servidor secundario en la BDD
     * @param entrada ip del servidor
     * @return Regresa 1 si fue exitoso, 0 si ya estaba o hubo algún error y otro si ya hay 3 servidores registrados
     */
    private int inscribirServidorSecundario(String entrada) {
            entrada = entrada.substring(18);
            BaseDeDatos bdd = new BaseDeDatos();
            return bdd.agregarServidorBDD(entrada, 1);
    }
}
