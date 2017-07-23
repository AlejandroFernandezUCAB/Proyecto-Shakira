/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

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
import BaseDeDatos.BaseDeDatos;
import java.util.StringTokenizer;

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
        //System.out.println("Escuchando: al socket" + ss);
        int suiche;
        try {
            
          //Establece el canal de entrada
          entrada = new BufferedReader(new InputStreamReader(ss.getInputStream()));
          // Establece canal de salida
          salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ss.getOutputStream())),true);
          
          //En este while se recibe la información del cliente y se procede a guardar en la base de datos
          //Además se le responde al cliente 
            
          
          
          while (true) {  
            String str = entrada.readLine();
              System.out.println(entrada);
            //-------INSCRIPCION----------
            
            if( str.contains("inscribirU")){
                
                suiche = inscribirUsuario(str);                                
                if( suiche == 1){
                   salida.println( "Servidor Central> Inscrito correctamente");                   
                }else{
                   salida.println( "Servidor Central> Ya ud se ha registrado");
                }
                
            }else if( str.trim().contains("inscribirS")){
                
                suiche = inscribirServidorSecundario(str);                                
                if( suiche == 1){
                    
                   salida.println( "Servidor Central > Servidor inscrito correctamente");                   
                   
                }else if (suiche == 0){
                    
                   salida.println( "Servidor Central > Ya ud se ha registrado");
                   
                }else{
                   salida.println( "Servidor Central > Ya hay 3 servidores registrados");
                }
                
            }
            
            //-------DESCARGA----------
            
            else if ( str.trim().contains("descarga")) {
                String ipCliente = ss.getInetAddress().toString().substring(1);
                System.out.println("entrada: " + str);
                System.out.println("Direccion IP del cliente: " + ipCliente);
                //verifico si el usuario esta inscrito
                    //verifico que el nombre del video existe
                if (this.verificarInscripcionUsuario(ipCliente)  == true    && 
                    this.verificarExistenciaVideo(this.extraerNombreVideo(str))     == false) 
                {
                    System.out.println("usuario Inscrito");
                    salida.println( "Servidor Central > Usuario registrado!....Peticion de descarga aprobada");
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
        
            entrada = entrada.substring(12);
            BaseDeDatos bdd = new BaseDeDatos();
            return bdd.agregarUsuarioBDD(entrada);
            
    }

    /**
     * Metodo que inscribe al servidor secundario en la BDD
     * @param entrada ip del servidor
     * @return Regresa 1 si fue exitoso, 0 si ya estaba o hubo algún error y otro si ya hay 3 servidores registrados
     */
    private int inscribirServidorSecundario(String entrada) {
        
            entrada = entrada.substring(12);
            BaseDeDatos bdd = new BaseDeDatos();
            return bdd.agregarServidorBDD(entrada);
            
    }
    
    
    private boolean verificarInscripcionUsuario(String entrada){
        BaseDeDatos bdd = new BaseDeDatos();
        return bdd.verificarInscripcionUsuario(entrada);
    }
    
    private boolean verificarExistenciaVideo(String nombreVid){
        System.out.println("Verificando la existencia del video: " + nombreVid);
        BaseDeDatos bdd = new BaseDeDatos();
        //esto devuelve true si el resultado es 1
            //de lo contrario si hubo un error o el video no existe devuelve false
        return (  bdd.videoExiste(nombreVid) == 1 );
    }
    
    private String extraerNombreVideo(String mensaje){
        return mensaje.substring(9);
    }
}
