/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaCliente;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 *
 * @author pedro
 */
public class Controladora extends Thread{
    private String comando;
    private JPanel panel;
    private JTextField input;
    private JTextArea output;
    private String inputString;
    
  /**
    * Constructor para inicializar el objeto
    * @param comando comando recibido por el cliente
    */
    Controladora(String comando) {
        this.comando = comando;
    }
    /**
     * Constructor para inicializar las diferentes cosas de la interfaz
     * @param panel
     * @param inputComando
     * @param consolaTextArea
     * @param inputComandoString 
     */
    public Controladora(JPanel panel, JTextField inputComando, JTextArea consolaTextArea, String inputComandoString) {
        this.panel = panel;
        this.input = inputComando;
        this.output = consolaTextArea;
        this.inputString = inputComandoString;
    }

      
    @Override
    public void run(){
        
        //En este if se verifica si la linea ingresada tiene inscribir, si es así procede a registrarse
        //El metodo que se llama para inscribir al servidor es inscribirServidor()
        
        if( inputString.trim().contains("inscribir")){
            
            output.setText( output.getText() + "Servidor Secundario > " + inputString + "\n");
            output.setText( output.getText() + inscribirServidor() + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
        }else if( comando.trim().contains( "videos_descargando" ) ){
            
        }else if ( comando.trim().contains( "videos_descargados" ) ){
            
        }else{
            System.err.println( "Servidor Secundario > Error en el comando" );
        }
        
    }

    private String inscribirServidor() {
        
        SocketConexionSecundario socket = new SocketConexionSecundario();
        return socket.inscribirServidor();
    }
}
