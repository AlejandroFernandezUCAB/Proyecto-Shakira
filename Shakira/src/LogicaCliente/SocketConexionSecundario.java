/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaCliente;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author gian
 */
public class SocketConexionSecundario {

    String descargarVideo(String ipServidorSec, int puertoServidorSec, String nombreVid, String rutaSeleccionada) throws IOException {
         BufferedReader entrada = null;
         PrintWriter salida = null;
         Socket s = null;
         
         //Inicializo la conexion con el socket
         try{
             s = new Socket(ipServidorSec,puertoServidorSec);
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
            String str = "descargar_"+nombreVid;
            // La envia al servidor
            salida.println(str);
            System.out.println("Se envio: "+ str);
            // Envía a la salida estándar la respuesta del servidor
            linea = entrada.readLine();
            System.out.println("Respuesta servidor: " + linea); 
            
            //-------------------------
             // Creamos flujo de entrada para leer los datos que envia el cliente 
               DataInputStream dis = new DataInputStream( s.getInputStream() );
        
               // Obtenemos el nombre del archivo
               String nombreArchivo = dis.readUTF().toString(); 
 
               // Obtenemos el tamaño del archivo
               int tam = dis.readInt(); 
 
               System.out.println( "Recibiendo archivo "+nombreArchivo + " de " + tam + " bits");
               
                // Creamos flujo de salida, este flujo nos sirve para 
               // indicar donde guardaremos el archivo
               FileOutputStream fos = new FileOutputStream( rutaSeleccionada +nombreArchivo );
               BufferedOutputStream out = new BufferedOutputStream( fos );
               BufferedInputStream in = new BufferedInputStream( s.getInputStream() );
 
               // Creamos el array de bytes para leer los datos del archivo
               byte[] buffer = new byte[ tam ];
 
               // Obtenemos el archivo mediante la lectura de bytes enviados
               for( int i = 0; i < buffer.length; i++ )
               {
                  buffer[ i ] = ( byte )in.read(); 
               }
 
               // Escribimos el archivo 
               out.write( buffer ); 
 
               // Cerramos flujos
               out.flush(); 
               in.close();
               out.close(); 
               s.close();
 
               System.out.println( "Archivo Recibido "+nombreArchivo );
            //-------------------------
            break;
          }
        } catch (Exception e) {
            return "Cliente > Problemas de conexión con el servidor secundario, intente más tarde";
        }
        
                // Libera recursos
        salida.close();
        entrada.close();
        stdIn.close();
        s.close();
        return linea;
    }
    
}
