/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.central;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

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
    try {
      BufferedReader in=new BufferedReader( new InputStreamReader( ss.getInputStream() ) );
            
            OutputStream out=ss.getOutputStream();
      boolean done=false;
      
      while (!done) {
          
	String str=in.readLine();
	if (str==null) 
            done=true;
	else { 
            
            System.out.println("Ip:" + str); 
            str = "Recibido";
            System.out.println(str);
            byte buf[] = str.getBytes();
            out.write(buf);
            done=true; 
            
        }
        
      }
      ss.close();
    }  catch (Exception e) { 
        
        System.out.println(e); 
        
    }
 }

}
