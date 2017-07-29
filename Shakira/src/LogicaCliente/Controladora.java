/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaCliente;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
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
    private String rutaArchivos;
    //el cliente tiene conocimientos previos de la ip del servidor y sus puertos
    String[] datosServidorCentral = {"192.168.0.2","1030"};
    //defino el puerto de los clientes (luego se puede parametrizar)
   // String puertoCliente = "1054";

    
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
    
        public Controladora(String rutaArchivos, String nombreUsuario, JPanel panel, JTextField inputComando, JTextArea consolaTextArea, String inputString) {
        this.rutaArchivos = rutaArchivos;
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
            
            //envio el comando al servidor central y recivo ip y puertos de los
            //servidores secunadrios
            String resultado = consultarServidoresSecundarios_ip_puerto(nombreVid);
            
            //numeros pares = ip servidores
            //numeros impares = puerto
            try{
                //---------------inicio try-------------
            if(!resultado.contains("Video no existe o usuario")){
            String[] infoServidores = this.extraerIPyPuertos(resultado);
            
            //esto no deberia dar error, porque para consultarServidoresSecundarios_ip_puerto servidores y clientes deben estar inscritos
            output.setText( output.getText() + "Enviando solicitud de descarga a " + infoServidores[0] + "\n");
                System.out.println("Envio a " + infoServidores[0]);
            this.descargarVideo(infoServidores[0], Integer.parseInt(infoServidores[1]), nombreVid);
            output.setText( output.getText() + "Enviando solicitud de descarga a " + infoServidores[2] + "\n");
                System.out.println("Envio a " + infoServidores[2]);
            this.descargarVideo(infoServidores[2], Integer.parseInt(infoServidores[3]), nombreVid);
            output.setText( output.getText() + "Enviando solicitud de descarga a " + infoServidores[4] + "\n");
                System.out.println("Envio a " + infoServidores[4]);
            this.descargarVideo(infoServidores[4], Integer.parseInt(infoServidores[5]), nombreVid);
            }
            else{
                output.setText( output.getText() + "Servidor Principal" + " > " + resultado + "\n");
            }
            
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            
            }catch(NumberFormatException e){
                    System.out.println("Excepcion: " + e);
                    }
            //---------------fin try-------------
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
                //System.out.println(comando);
            }
        }
        return comando;
    }
    
        public String[] extraerIPyPuertos(String str){
        String[] campos = new String[7];
        int i = 0;
        StringTokenizer tokens = new StringTokenizer(str,"_");
        while(tokens.hasMoreTokens()){
             campos[i] = tokens.nextToken();
             System.out.println(campos[i]);
             i++;
             campos[i] = tokens.nextToken();
             System.out.println(campos[i]);
             i++;
        }
        return campos;
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
        (datosServidorCentral[0] , Integer.parseInt(datosServidorCentral[1]), this.nombreUsuario );
                //s.conexionPrueba(puertos);
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        
            return resultado;
            
    }
    
    /**
     *Envio el nombre del video al servidor principal, este verifica
     * si estoy inscrito y si el video existe, de ser asi, aprueba la
     * peticion y continua con la siguiente fase de la descarga.
     * @param nombreVid nombre del video a consultarServidoresSecundarios_ip_puerto
     * @return mensaje del servidor
     */
    public String consultarServidoresSecundarios_ip_puerto(String nombreVid){
            String resultado = null;
            try{
                SocketConexionPrincipal s = new SocketConexionPrincipal();
                //resultado = s.inscribirUsuario
        //(datosServidorCentral[0] , Integer.parseInt(datosServidorCentral[1]), this.puertos );
                resultado = s.enviarComandoDescarga
        (datosServidorCentral[0] , Integer.parseInt(datosServidorCentral[1]),nombreVid);
                
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        
            return resultado;
            
    }
    
    public String descargarVideo(String ipServidorSec,int puertoServidorSec ,String nombreVid){
        String resultado = null;
        try{
            SocketConexionSecundario s = new SocketConexionSecundario();
            resultado = s.descargarVideo(ipServidorSec,puertoServidorSec, nombreVid,this.rutaArchivos);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        return resultado;
    }
    
}
