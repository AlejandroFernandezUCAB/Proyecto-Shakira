/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.central;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
      PrintWriter out=new PrintWriter( ss.getOutputStream() ,true );
      System.out.println("Hola! Escribir BYE para salir");
      boolean done=false;
      
      while (!done) {
          
	String str=in.readLine();
	if (str==null) 
            done=true;
	else { 
            
            System.out.println("Echo:("+counter+"):"+str); 
	    if (str.trim().equals("BYE")) 
            done=true; 
            
        }
        
      }
      ss.close();
    }  catch (Exception e) { 
        
        System.out.println(e); 
        
    }
 }

}
