/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaServidor;

import BaseDeDatos.BaseDeDatos;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Alejandro Fernandez
 */
public class SocketConexionSecundario extends Thread{
    
    private Socket ss;
    private int counter;
    
    public SocketConexionSecundario(Socket ss, int i) {
        this.ss = ss;
        this.counter = i;
        /*
        BufferedReader entrada = null;
    
    SocketConexionSecundario(Socket ss, int i) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        enviarArchivo(ss,i);
    }
    
    */
    }
    
    
    @Override
    public void run(){
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
            System.out.println(str);
            //-------Enviando Video a Servidor Secundario---------- 
                if( str.contains("descargars")){
                    System.out.println("---------------Enviando Archivo a Servidor Secundario--------------");
                    suiche = enviarVideoSecundario(str);                                
                    if( suiche == 1){
                       salida.println( "Servidor Secundario > Enviado correctamente");                   
                    }else{
                       salida.println( "Servidor Secundario > Falló el envío");
                    }
                } 
                else if (str.contains("descargar")) {
                   this.enviarArchivo(ss);
               }
                break;
            }

    
        }catch(IOException e){
             System.out.println(e.getMessage());
        }
    }
    /**
     * Se envia el video al servidor secundario
     * @param nombreVideo Nombre del video a buscar
     * @return 1 si es eitoso, 0 si fallo
     */
    private int enviarVideoSecundario(String nombreVideo) {
            BaseDeDatos bd = new BaseDeDatos();
            String rutaArchivo = bd.rutaVideo(nombreVideo);
            try{
                // Creamos el archivo que vamos a enviar
                File archivo = new File( rutaArchivo );

                // Obtenemos el tamaño del archivo
                int tamañoArchivo = ( int )archivo.length();

                // Creamos el flujo de salida, este tipo de flujo nos permite 
                // hacer la escritura de diferentes tipos de datos tales como
                // Strings, boolean, caracteres y la familia de enteros, etc.
                DataOutputStream dos = new DataOutputStream( ss.getOutputStream() );

                System.out.println( "Enviando Archivo: "+archivo.getName() );

                // Enviamos el nombre del archivo 
                dos.writeUTF( archivo.getName() );

                // Enviamos el tamaño del archivo
                dos.writeInt( tamañoArchivo );

                // Creamos flujo de entrada para realizar la lectura del archivo en bytes
                FileInputStream fis = new FileInputStream( rutaArchivo );
                BufferedInputStream bis = new BufferedInputStream( fis );

                // Creamos el flujo de salida para enviar los datos del archivo en bytes
                BufferedOutputStream bos = new BufferedOutputStream( ss.getOutputStream() );

                // Creamos un array de tipo byte con el tamaño del archivo 
                byte[] buffer = new byte[ tamañoArchivo ];

                // Leemos el archivo y lo introducimos en el array de bytes 
                bis.read( buffer ); 

                // Realizamos el envio de los bytes que conforman el archivo
                for( int i = 0; i < buffer.length; i++ )
                {
                    bos.write( buffer[ i ] ); 
                } 

                System.out.println( "Archivo Enviado: "+archivo.getName() );
                // Cerramos socket y flujos
                bis.close();
                bos.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
                return 0;
            }
        return 1;
    }
    private int parteAsignadaVideo(String nombreVid){
        BaseDeDatos bdd = new BaseDeDatos();
        return bdd.parteAsignadaDeVideo(nombreVid);
    }
    
    private String rutaVideo(String nombreVid){
        BaseDeDatos bdd = new BaseDeDatos();
        return bdd.rutaDeVideo(nombreVid);
    }
    
    public synchronized void enviarArchivo(Socket ss){
                    BufferedReader entrada = null;
        PrintWriter salida = null;
        //System.out.println("Escuchando: al socket" + ss);
          try{  
            //Establece el canal de entrada
            entrada = new BufferedReader(new InputStreamReader(ss.getInputStream()));
            // Establece canal de salida
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ss.getOutputStream())),true);
            while (true) {  
                String str = entrada.readLine();
                System.out.println(str);
                salida.println("Servidor Secundario > enviando video");
                
                //--------------envio el archivo------------
                try{
                        //IP de la maquina que recibira el archivo
                        InetAddress direccion = ss.getInetAddress();
                        //No hace falta que creemos otro socket
                        //usamos esta misma conexion

                        String nombreArchivo = str.substring(10);//quito: descargar_
                        
                        // Creamos el archivo que vamos a enviar
                       // String ruta = "/home/gian/Desktop/"+ nombreArchivo; // esto se va a quitar
                        int parteAsignada = parteAsignadaVideo(nombreArchivo);
                        System.out.println("parte del video asignada: "+ parteAsignada);
                        
                        //si devolvio 0 es porque o no existe el video o hubo algun error
                        if(parteAsignada != 0){
                        String ruta = this.rutaVideo(nombreArchivo)+nombreArchivo;
                        File archivo = new File( ruta );

                        // Obtenemos el tamaño del archivo
                        int tamañoArchivo = ( int )archivo.length(); 

                        
                        
                        // Creamos el flujo de salida, este tipo de flujo nos permite 
                        // hacer la escritura de diferentes tipos de datos tales como
                        // Strings, boolean, caracteres y la familia de enteros, etc.
                        DataOutputStream dos = new DataOutputStream( ss.getOutputStream() );

                        System.out.println( "Enviando Archivo: "+archivo.getName()+".parte" + parteAsignada );
                        
                        
                        // Enviamos el nombre del archivo 
                        dos.writeUTF( archivo.getName() + ".parte" + parteAsignada);

                        // Enviamos el tamaño del archivo
                       // dos.writeInt( tamañoArchivo );
                        

                         // Creamos flujo de entrada para realizar la lectura del archivo en bytes
                        try{
                            /*
                            este parametro fue lo que me fregaba la vida
                            el archivo lo enviaba pero con basura...aunque del tamaño correcto
                            habia cambiado el nombre del archivo que recibo en 'file',
                            colocando toda la ruta.
                            pero aca no, y solo recibia el nombre y no la ruta...
                            */
        
                         FileInputStream fis = new FileInputStream( ruta );
                        //AQUI HAGO EL SKIPPP
                        int parte = Math.round(tamañoArchivo/3);
                        int inicio = 0;
                        int tope = 0;
                        
                        switch(parteAsignada){
                            case 1:
                                //fis.skip(tamañoArchivo);
                                inicio = 0;
                                tope = parte;
                                dos.writeInt( tamañoArchivo );
                                break;
                            case 2:
                                inicio = parte;
                                tope = parte*2;
                                dos.writeInt( inicio-tope );
                                break;
                            case 3:
                                inicio = parte*2;
                                tope = tamañoArchivo;
                                dos.writeInt( inicio-tope );
                                break;
                            default:
                                return;
                        }
                        
                        fis.skip(inicio);
                         
                        
                        BufferedInputStream bis = new BufferedInputStream( fis );

                        // Creamos el flujo de salida para enviar los datos del archivo en bytes
                        BufferedOutputStream bos = new BufferedOutputStream( ss.getOutputStream()          );

                        // Creamos un array de tipo byte con el tamaño del archivo 
                        byte[] buffer = new byte[ tamañoArchivo ];

                        // Leemos el archivo y lo introducimos en el array de bytes 
                        bis.read( buffer ); 

                        // Realizamos el envio de los bytes que conforman el archivo
                       // for( int x = 0; x < buffer.length; x++ )
                       for( int x = 0; x < tope; x++ )
                        {
                            bos.write( buffer[ x ] ); 
                        } 

                        System.out.println( "Archivo Enviado: "+archivo.getName() );
                        // Cerramos socket y flujos
                        bis.close();
                        bos.close(); }
                        catch(FileNotFoundException e){
                            System.out.println("No se encontro e archivo");
                        }
                        
                    }
                }
                catch(Exception e){
                    System.out.println(e.toString());
                }
                    
                
                //--------------termino envio---------------
                
                  break;
            }
          }catch (IOException e) {
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
    
}

//use este mismo ejemplo
//http://www.geocities.ws/programmiersprache/envioarchivo.html