/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

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
public class Controladora extends Thread{
    private JPanel consola;
    private JTextField input;
    private JTextArea output;
    private String nombreUsuario, inputString;

    public Controladora(JPanel consola,JTextField input, JTextArea output) {
        this.consola = consola;
        this.input = input;
        this.output = output;
    }

    public Controladora(String nombreUsuario, JPanel panel, JTextField inputComando, JTextArea consolaTextArea) {
        
        this.nombreUsuario = nombreUsuario;
        this.consola = panel;
        this.input = inputComando;
        this.output = consolaTextArea;
        
    }

    public Controladora(JPanel consola, JTextField input, JTextArea output, String nombreUsuario, String inputString) {
        this.consola = consola;
        this.input = input;
        this.output = output;
        this.nombreUsuario = nombreUsuario;
        this.inputString = inputString;
    }   

    public Controladora(String nombreUsuario, JPanel panel, JTextField inputComando, JTextArea consolaTextArea, String inputString) {
        this.consola = panel;
        this.input = inputComando;
        this.output = consolaTextArea;
        this.nombreUsuario = nombreUsuario;
        this.inputString = inputString;
    }
/**
 * Enviar información al servidor y lo ejecuta como un hilo, para así la consola siga viva
 * El primer if corresponde a que el comando esté introducido correctamente
 * El segundo If verifica si lo que el cliente escribio posee "inscribir" en el segundo ourput.setText se hace llamada a la conexion de socket
 * y regresa un string el cual es el encargado de decir si fue con exito o no la inscripcion
 * El Else es en caso de que el comando este mal escrito 
 */    
    public void run (){

        if(inputString.isEmpty() || inputString == null || inputString.equalsIgnoreCase("inserte comando aquí") ){
            
            JOptionPane.showMessageDialog( consola , "El comando que introdujo es erroneo. \nIntente nuevamente" , "¡Error de comando!", JOptionPane.ERROR_MESSAGE);
           
        }else if( sacarInscribir(inputString).equalsIgnoreCase("inscribir") ){
            
            output.setText( output.getText() + nombreUsuario + " > " + inputString + "\n");
            output.setText( output.getText() + inscribirUsuario() + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            
        }else{
            output.setText( output.getText() + nombreUsuario + " > " + inputString + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            output.setText( output.getText() + "Cliente > Error en el comando\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
        }
            
    }
    
    /**
     * Metodo que verifica si está escrito el comando inscribir
     * @param inputString recibe el comando completo
     * @return devuelve las primeras 9 letras para ver si es inscribir
     */
    public String sacarInscribir(String inputString){
        String comando = "";
        if (inputString.length() > 8){
            for (int i = 0; i < 9; i++) {
            
                comando = comando + inputString.charAt(i);        
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
