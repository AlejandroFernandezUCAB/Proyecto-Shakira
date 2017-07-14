/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Alejandro Fernandez
 */
public class SocketConexion {
    
    /**
     * Probando Hilo
     */
    public void conexionPrueba() {
        int c;
        try{
            Socket s=new Socket("192.168.0.2",500);
            InputStream in=s.getInputStream();
            OutputStream out=s.getOutputStream();
            String str="Hola menor";
            byte buf[]=str.getBytes();
            out.write(buf);
            s.close();
            
        }catch(Exception e) {
            
            System.out.println(e);
            
        }
    }
    
    
    public void inscribirUsuario(String direccionIp, int puerto){
            
        try{
            
            Socket s=new Socket("192.168.0.2",500);
            InputStream in=s.getInputStream();
            OutputStream out=s.getOutputStream();
            InetAddress adress = InetAddress.getLocalHost();
            String str = adress.getHostAddress().toString();
            System.out.println(str);
            byte buf[] = str.getBytes();
            out.write(buf);
            s.close();
            
            }catch(IOException e){
                
                System.out.println(e);
                
            }
    }
}
