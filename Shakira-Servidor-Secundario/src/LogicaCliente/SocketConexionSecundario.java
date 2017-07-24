/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaCliente;

import BaseDeDatos.BaseDeDatos;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

    
    public String inscribirServidor(String ipServidorCentral, int puertoCentral, String[] puertos) {
        BufferedReader entrada = null;
        PrintWriter salida = null;
        Socket s = null;
        BufferedReader stdIn =	new BufferedReader(new InputStreamReader(System.in));
        String linea=null;
        String[] nombreVideos = null;
        
         //Inicializo la conexion con el socket
         try{
            s = new Socket( ipServidorCentral , puertoCentral);
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
            //A partir de aquí comienza el proceso de sincronización
            BaseDeDatos bd = new BaseDeDatos();
            //Aqui recibe un array de string con el nombre de cada video;
            nombreVideos = bd.nombreDeVideos();
            //El digo la cantidad de vídeos que voy a enviar
            salida.println( nombreVideos.length );
              //Realizo el for para enviar el nombre de cada video automatico
              for (String nombreVideo : nombreVideos) {
                  salida.println(nombreVideo);                  
              }
            //Se envian los archivos por el socket al servidor central
            // Codigo para envio por socket de : http://www.geocities.ws/programmiersprache/envioarchivo.html
             String nombreArchivo = "C:\\Videos\\Sword_Art_Online_Theme_Swordland_Violin_Cover_Taylor_Davis(youtube.com).mp4";
             File archivo = new File( nombreArchivo );
         
            // Obtenemos el tamaño del archivo
            int tamañoArchivo = ( int )archivo.length();
         
            // Creamos el flujo de salida, este tipo de flujo nos permite 
            // hacer la escritura de diferentes tipos de datos tales como
            // Strings, boolean, caracteres y la familia de enteros, etc.
            DataOutputStream dos = new DataOutputStream( s.getOutputStream() );
         
            System.out.println( "Enviando Archivo: "+archivo.getName() );
         
            // Enviamos el nombre del archivo 
            salida.println( archivo.getName() ); 
         
            // Enviamos el tamaño del archivo
            salida.println( tamañoArchivo );
         
            // Creamos flujo de entrada para realizar la lectura del archivo en bytes
            FileInputStream fis = new FileInputStream( nombreArchivo );
            BufferedInputStream bis = new BufferedInputStream( fis );
         
            // Creamos el flujo de salida para enviar los datos del archivo en bytes
            BufferedOutputStream bos = new BufferedOutputStream( s.getOutputStream());
         
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
            //Recepcion de los videos del central
            //Recibo la cantidad de videos
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
