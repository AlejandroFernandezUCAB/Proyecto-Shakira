/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaServidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
