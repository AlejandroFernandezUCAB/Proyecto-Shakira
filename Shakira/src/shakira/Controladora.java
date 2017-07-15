/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import sun.security.util.Length;

/**
 *
 * @author pedro
 */
public class Controladora {
    private JPanel consola;
    private JTextField input;
    private JTextArea output;
    private String nombreUsuario;

    public Controladora(JPanel consola,JTextField input, JTextArea output) {
        this.consola = consola;
        this.input = input;
        this.output = output;
    }

    Controladora(String nombreUsuario, JPanel panel, JTextField inputComando, JTextArea consolaTextArea) {
        
        this.nombreUsuario = nombreUsuario;
        this.consola = panel;
        this.input = inputComando;
        this.output = consolaTextArea;
        
    }
    
/**
 * Enviar información al servidor, por los momentos pura interfaz
 */    
    public void enviarInformacion (){
        
        String inputToString = input.getText().trim().toLowerCase();
        if(inputToString.isEmpty() || inputToString == null || inputToString.equalsIgnoreCase("inserte comando aquí")){
            
            JOptionPane.showMessageDialog( consola , "El comando que introdujo es erroneo. \nIntente nuevamente" , "¡Error de comando!", JOptionPane.ERROR_MESSAGE);
           
        }else if( sacarInscribir(inputToString).equalsIgnoreCase("inscribir") ){
            
            inscribirUsuario();
            
        }else{
            output.setText( output.getText() + nombreUsuario + " > " + inputToString + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);

            try{
                
                InetAddress adress = InetAddress.getLocalHost();
                output.setText( output.getText() + nombreUsuario + " > " + "Su dirección ip es:"+ adress + "\n");
                output.setLineWrap(true);
                output.setWrapStyleWord(true);
                
            }catch(UnknownHostException e){
                
            }
            
        }
            
    }
    
    /**
     * Metodo que verifica si está escrito el comando inscribir
     * @param inputToString recibe el comando completo
     * @return devuelve las primeras 9 letras para ver si es inscribir
     */
    public String sacarInscribir(String inputToString){
        String comando = "";
        if (inputToString.length() > 8){
            for (int i = 0; i < 9; i++) {
            
                comando = comando + inputToString.charAt(i);        
            }
        }
        return comando;
    }
    
    /**
     * Envía la informacion a a controladora de sockets 
     * Se saca el ip local y se envia
     */
    public void inscribirUsuario(){
        try{
            InetAddress adress = InetAddress.getLocalHost();
            SocketConexion s = new SocketConexion();
            s.inscribirUsuario( adress.toString() , 1 );
        }catch (UnknownHostException e){
            
        }
    }
}
