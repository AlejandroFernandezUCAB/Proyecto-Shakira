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
              String[] rutaVideos = bd.rutaDeVideos();
              for (String rutaVideo : rutaVideos) {
                  File archivoAEnviar = new File(rutaVideo);
                  int tamañoArchivo = ( int ) archivoAEnviar.length();
                  System.out.println("Servidor Central > Enviando Archivo: " + archivoAEnviar.getName() + "Tamaño "
                  + tamañoArchivo);
                  //Se envia el nombre del archivo
                  salida.println( archivoAEnviar.getName() );
                  //Se envia el tamaño del archivo
                  salida.println( tamañoArchivo );
                  //Manejo de archivo y sockets
                  FileInputStream fis = new FileInputStream( archivoAEnviar );
                  BufferedInputStream bis = new BufferedInputStream(fis);
                  BufferedOutputStream bos = new BufferedOutputStream( s.getOutputStream() );
                  //Se crea un buffer con el tamaño del archivo
                  byte[] buffer = new byte[ tamañoArchivo ];
                  //Se lee y se introduce en el arrayde bytes
                  bis.read( buffer );
                  //Se realiza el envío
                  int count;
                  while ((count = fis.read(buffer)) > 0) {
                      bos.write(buffer, 0, count);
                      Thread.sleep(50);
                  }
                  /*for (int j = 0; j < buffer.length; j++) {
                      bos.write( buffer[j] );
                  }
                  */
                  System.out.println(entrada.readLine()); 
              } //Fin de enviar los videos
             
            //Recepcion de los videos del central
            //Recibo la cantidad de videos
            str = entrada.readLine();
            int videosQueLlegaran = Integer.parseInt(str);
            //Por cada video que venga hago se hace un ciclo
            for (int i = 0; i < videosQueLlegaran ; i++) {
                //Recibo el nombre del video
                str = entrada.readLine();
                //Se recibe el tamaño
                int tamaño = Integer.parseInt( entrada.readLine() );
                System.out.println("Servidor Central > Recibiendo el archivo "+ str +
                        " con un tamaño de " + tamaño);
                FileOutputStream fos = new FileOutputStream("C:\\prueba\\" + str);
                BufferedOutputStream out = new BufferedOutputStream(fos);
                byte[] buffer = new byte[ tamaño ];
                for (int j = 0; i < buffer.length; j++) {
                    buffer[j] = (byte)entrada.read();
                }
                bd.agregarVideoServidorSecundario( entrada.readLine(), 1 ); //Meto uno mientras tanto pero tengo que recibir tambien que parte es
                out.write( buffer );
                fos.close();
                out.close();  
            }
            
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
