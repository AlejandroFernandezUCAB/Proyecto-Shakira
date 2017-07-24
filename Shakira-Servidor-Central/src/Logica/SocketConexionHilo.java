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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
            
          //System.out.println("Direccion IP del cliente: " + ss.getInetAddress());
          
          //System.out.println("entrada: " + entrada);
          while (true) {  
            String str = entrada.readLine();

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
                   sincronizacion(str, entrada, salida);
                   
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

    /**
     * Metodo encargado de realizar la sincronización
     * @param str Aqui entrará todo lo nuevo
     * @param entrada Canal de entrada
     * @param salida Canal de salida
     */
    private void sincronizacion(String str, BufferedReader entrada, PrintWriter salida) {
        try{
            BaseDeDatos bd = new BaseDeDatos();
            str = entrada.readLine();
            int videosQueMeLLegaran = Integer.parseInt(str);
            for (int i = 0; i < videosQueMeLLegaran; i++) {
                //Recibo los videos
                str = entrada.readLine();
                System.out.println(str);
                //Se procede a guardar y se verifica si es true se guardo
                if( bd.agregarVideoSincronizacion(str) == true) {
                    System.out.println("Servidor central > El Video: " + str
                            + " se guardo correctamente");
                }
                
            }
            //Aqui se procede a recibir los archivos
            
            //Se recibe el nombre del archivo
            recibirArchivo(str, entrada, salida, videosQueMeLLegaran);
            //Fin de recepcion de archivos
            //Se verifica que hayan 3 servidores inscritos y que ya hayan enviado los archivos
            boolean suiche = true;
            while(suiche == true){
                
                if(bd.verificarServidores() == true){
                    suiche=false;
                }
                
            }
            //Fin de envio de los 3 servidores
            //Ahora como ya están los 3 servidores se procede a enviar cada archivo
            enviarArchivos(str, entrada, salida);
            
        }catch(IOException e){
            
        }
    }
    
    /**
     * Metodo en el cual recibe cada video
     * @param str Aqui es donde llegará cada item
     * @param entrada Canal de entrada
     * @param salida Canal de Salida
     * @param cantidadVideos Cantidad de videos que recibiré
     */
    private void recibirArchivo(String str, BufferedReader entrada, PrintWriter salida, int cantidadVideos){
        for (int i = 0; i < cantidadVideos; i++) {
            try{
                str = entrada.readLine();
                //Se recibe el tamaño
                int tamaño = Integer.parseInt( entrada.readLine() );
                System.out.println("Servidor Central > Recibiendo el archivo "+ str +
                        " con un tamaño de " + tamaño);
                FileOutputStream fos = new FileOutputStream("C:\\prueba\\" + str);
                BufferedOutputStream out = new BufferedOutputStream(fos);
                byte[] buffer = new byte[ tamaño ];
                for (int j = 0; j < buffer.length; i++) {
                    buffer[j] = (byte)entrada.read();
                }
                out.write( buffer );
                fos.close();
                out.close();
            }catch (IOException e){
                System.out.println("No se pudo guardar el archivo");
            }
        }
    }
    /**
     * Enviar cada video alojado en el servidor
     * @param str String de entrada
     * @param entrada canal de entrada
     * @param salida canal de salida
     */
    private void enviarArchivos(String str, BufferedReader entrada, PrintWriter salida){
        
        try{
            BaseDeDatos bd = new BaseDeDatos();
            int videosQueEnviare = bd.cantidadVideosAlojados();
            String[] videos = bd.videosAlojados( videosQueEnviare );
            for (String video : videos) {
                File archivoAEnviar = new File("C:\\prueba\\" + video); //Estoy agregando esta ruta por defecto
                int tamañoArchivo = ( int ) archivoAEnviar.length();
                System.out.println("Servidor Central > Enviando Archivo: " + archivoAEnviar.getName() );
                //Se envia el nombre del archivo
                salida.println( archivoAEnviar.getName() );
                //Se envia el tamaño del archivo
                salida.println( tamañoArchivo );
                //Manejo de archivo y sockets
                FileInputStream fis = new FileInputStream( archivoAEnviar );
                BufferedInputStream bis = new BufferedInputStream(fis);
                BufferedOutputStream bos = new BufferedOutputStream( ss.getOutputStream() );
                //Se crea un buffer con el tamaño del archivo
                byte[] buffer = new byte[ tamañoArchivo ];
                //Se lee y se introduce en el arrayde bytes
                bis.read( buffer );
                //Se realiza el envío
                for (int j = 0; j < buffer.length; j++) {
                    bos.write( buffer[j] );
                }
            }
        }catch(IOException e){
            
            System.out.println( e.getMessage() );
            
        }
    }
}
