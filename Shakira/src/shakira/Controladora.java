/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira;

import java.io.IOException;
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
        if(inputToString.isEmpty() || inputToString == null || inputToString.equalsIgnoreCase("inserte comando aquí") ){
            
            JOptionPane.showMessageDialog( consola , "El comando que introdujo es erroneo. \nIntente nuevamente" , "¡Error de comando!", JOptionPane.ERROR_MESSAGE);
           
        }else if( sacarInscribir(inputToString).equalsIgnoreCase("inscribir") ){
            
            output.setText( output.getText() + nombreUsuario + " > " + input.getText() + "\n");
            output.setText( output.getText() + inscribirUsuario() + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            
        }else{
            output.setText( output.getText() + nombreUsuario + " > " + input.getText() + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            output.setText( output.getText() + "Cliente > Error en el comando\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
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
     * @return mensaje del servidor
     */
    public String inscribirUsuario(){
            String resultado = null;
            try{
                InetAddress adress = InetAddress.getLocalHost();
                SocketConexion s = new SocketConexion();
                resultado = s.inscribirUsuario( adress.getHostAddress() , 1 );
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        
            return resultado;
            
    }
}
