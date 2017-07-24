/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaCliente;

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
        
    //el cliente tiene conocimientos previos de la ip del servidor y sus puertos
    String[] datosServidorCentral = {"192.168.0.2","1026"};
    String puertoCliente = "1024";
    
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
           
        }else if( extraerComando(inputString, 9).equalsIgnoreCase("inscribir") ){
            
            output.setText( output.getText() + nombreUsuario + " > " + inputString + "\n");
            output.setText( output.getText() + inscribirUsuario() + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            
        }else if( extraerComando(inputString,5).equalsIgnoreCase("video") ){
            
            System.out.println("comando para descargar videos");
            try{
            String nombreVid = inputString.substring(6);
            System.out.println("nombreVid = " + nombreVid);
            output.setText( output.getText() + nombreUsuario + " > " + inputString + "\n");
            output.setText( output.getText() + descargar(nombreVid) + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            }
            catch(StringIndexOutOfBoundsException e){
                output.setText(output.getText() + "Cliente > Debe especificar el nombre del video" + "\n");
            }            
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
     * @param size Tamaño de la cadena (empezando a contar desde 1)
     * @return devuelve las primeras n=(size) letras para ver si es un comando
     */
    public String extraerComando(String inputString, int size){
        String comando = "";
        if (inputString.length() >= size){
            for (int i = 0; i < size; i++) {
            
                comando = comando + inputString.charAt(i); 
                System.out.println(comando);
            }
        }
        return comando;
    }
    
    /**
     * Envía la informacion a a controladora de sockets 
     * Se saca pasa la ip del servidor Central y los puertos de
     * este cliente para que lo inscriba
     * @return mensaje del servidor
     */
    public String inscribirUsuario(){
            String resultado = null;
            try{
                //InetAddress adress = InetAddress.getLocalHost();
                SocketConexionPrincipal s = new SocketConexionPrincipal();
                resultado = s.inscribirUsuario
        (datosServidorCentral[0] , Integer.parseInt(datosServidorCentral[1]), puertoCliente );
                //s.conexionPrueba(puertos);
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        
            return resultado;
            
    }
    
    /**
     *Envio el nombre del video al servidor principal, este verifica
     * si estoy inscrito, de ser asi, aprueba la peticion y continua con
     * la siguiente fase de la descarga.
     * @param nombreVid nombre del video a descargar
     * @return mensaje del servidor
     */
    public String descargar(String nombreVid){
            String resultado = null;
            try{
                SocketConexionSecundario s = new SocketConexionSecundario();
                //resultado = s.inscribirUsuario
        //(datosServidorCentral[0] , Integer.parseInt(datosServidorCentral[1]), this.puertos );
                resultado = s.descargarVid
        (datosServidorCentral[0] , Integer.parseInt(datosServidorCentral[1]),nombreVid);
                
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        
            return resultado;
            
    }
    
    
    
}
