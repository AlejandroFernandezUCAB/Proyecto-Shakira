/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaServidor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Alejandro Fernandez
 */
public class SocketConexionSecundario extends Thread{

    SocketConexionSecundario(Socket ss, int i) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                        //IP de la maquina que recibira el archivo
                        InetAddress direccion = ss.getInetAddress();
                        //No hace falta que creemos otro socket
                        //usamos esta misma conexion

                        String nombreArchivo = str.substring(9);//quito: descargar_

                        // Creamos el archivo que vamos a enviar
                        File archivo = new File( nombreArchivo );

                        // Obtenemos el tamaño del archivo
                        int tamañoArchivo = ( int )archivo.length(); 

                        /*
                        
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
                        FileInputStream fis = new FileInputStream( nombreArchivo );
                        BufferedInputStream bis = new BufferedInputStream( fis );

                        // Creamos el flujo de salida para enviar los datos del archivo en bytes
                        BufferedOutputStream bos = new BufferedOutputStream( ss.getOutputStream()          );

                        // Creamos un array de tipo byte con el tamaño del archivo 
                        byte[] buffer = new byte[ tamañoArchivo ];

                        // Leemos el archivo y lo introducimos en el array de bytes 
                        bis.read( buffer ); 

                        // Realizamos el envio de los bytes que conforman el archivo
                        for( int x = 0; x < buffer.length; x++ )
                        {
                            bos.write( buffer[ x ] ); 
                        } 

                        System.out.println( "Archivo Enviado: "+archivo.getName() );
                        // Cerramos socket y flujos
                        bis.close();
                        bos.close();    
                    */
                
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
