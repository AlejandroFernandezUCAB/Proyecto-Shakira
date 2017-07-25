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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

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
            s = new Socket( ipServidorCentral, puertoCentral);
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
            System.out.println("Servidor Central > Sincronización completa, esperando por los demás servidores");
            //Se espera respuesta del servidor para saber donde se encuentra cada video
            linea = entrada.readLine();
            System.out.println( linea );
            //Se recibe cuantos videos se van a descargar
            String[] videosIpPuerto = new String[ Integer.parseInt(entrada.readLine())];
            //Se reciben el nombre del video, la ip y el puerto
            //y su puerto para descargarlo
            for (int i = 0; i < videosIpPuerto.length; i++) {
                  videosIpPuerto[i] = entrada.readLine();       
            }
            
            peticionDeDescargaAServidoresSecundarios( videosIpPuerto );
                        
            break;
            
          }
           
           // Libera recursos
           salida.close();
           entrada.close();
           stdIn.close();
           s.close();
           //Hago la peticion de descargas
           
           
        } catch (IOException e) {
            
            return ("Servidor Secundario > Problemas de conexión con el servidor, intente más tarde"); 
            
        } catch (Exception e){
            
            return ("Servidor Secundario > Ha sucedido un error inesperado, intente nuevamente");
            
        }
        
        return linea;
        
    }

    private void peticionDeDescargaAServidoresSecundarios(String[] videosIpPuerto) {
       
        String[] campos = new String[videosIpPuerto.length];
        int i = 0;
        for (int j = 0; j < videosIpPuerto.length ; j++) { 
            StringTokenizer tokens = new StringTokenizer(videosIpPuerto[j],"_");
            while(tokens.hasMoreTokens()){
                 campos[j] = tokens.nextToken();
                 System.out.println(campos[j]);
                 i++;
            }
            descarga(campos);
            i=0;
        }
    }

    private void descarga(String[] campos) {
        BufferedReader entrada = null;
        PrintWriter salida = null;
        Socket s = null; 
        try
          {
               // Creamos el Socket con la direccion y elpuerto de comunicacion
               s = new Socket( campos[1], Integer.parseInt( campos[2]) );
               s.setSoTimeout( 2000 );
               s.setKeepAlive( true );
               //Canal de Entrada
               entrada = new BufferedReader(new InputStreamReader( s.getInputStream() ) );
               // Obtenemos el canal de salida
               salida = new PrintWriter(new BufferedWriter( new OutputStreamWriter( s.getOutputStream() ) ),true);
               //Envio por el canal el video que quiero
               salida.println( "descargars"+campos[0] );
               
               // Creamos flujo de entrada para leer los datos que envia el cliente 
               DataInputStream dis = new DataInputStream( s.getInputStream() );
        
               // Obtenemos el nombre del archivo
               String nombreArchivo = dis.readUTF(); 
 
               // Obtenemos el tamaño del archivo
               int tam = dis.readInt(); 
 
               System.out.println( "Recibiendo archivo: "+ nombreArchivo );
        
               // Creamos flujo de salida, este flujo nos sirve para 
               // indicar donde guardaremos el archivo
               FileOutputStream fos = new FileOutputStream( "C:\\prueba\\"+nombreArchivo );
               BufferedOutputStream out = new BufferedOutputStream( fos );
               BufferedInputStream in = new BufferedInputStream( s.getInputStream() );
 
               // Creamos el array de bytes para leer los datos del archivo
               byte[] buffer = new byte[ tam ];
 
               // Obtenemos el archivo mediante la lectura de bytes enviados
               for( int i = 0; i < buffer.length; i++ )
               {
                  buffer[ i ] = ( byte )in.read( ); 
               }
 
               // Escribimos el archivo 
               out.write( buffer ); 
 
               // Cerramos flujos
               out.flush(); 
               in.close();
               out.close(); 
               s.close();
               entrada.close();
               salida.close();
               System.out.println( "Archivo Recibido "+nombreArchivo );
          }
          catch( Exception e )
          {
            System.out.println( e.getMessage() );
            
          }finally{
              
          }
         
    }
    
}
