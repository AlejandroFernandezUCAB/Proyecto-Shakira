/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.secundario;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author pedro
 */
public class Controladora extends Thread{
    private String comando;

      /**
    * Constructor para inicializar el objeto
    * @param comando comando recibido por el cliente
    */
    Controladora(String comando) {
        this.comando = comando;
    }
    
    
    @Override
    public void run(){
        
        if( comando.trim().contains("inscribir")){
            
            SocketConexionSecundario socket = new SocketConexionSecundario();
            socket.inscribirServidor();
            
        }else if( comando.trim().contains( "videos_descargando" ) ){
            
        }else if ( comando.trim().contains( "videos_descargados" ) ){
        
        }else{
            System.err.println( "Servidor Secundario > Error en el comando" );
        }
        
    }
}
