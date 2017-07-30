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
    public String nombreBaseDeDatos = "servidorSecundario";
    private String ruta = "/home/gian/videosDescargados";
    //puertos del servidor secundario {}
    public static String[] puertosServSecundario = {"1026","1027"};
    //los servidores secundarios tiene conocimiento previo
    //de la ip del servidor central y su puerto
    String[] datosServidorCentral = {"192.168.0.2","1030"};
    
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

    public Controladora(String ruta, JPanel panel, JTextField inputComando, JTextArea consolaTextArea, String inputComandoString) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(!ruta.isEmpty() && ruta != null)
            this.ruta = ruta;
        this.panel = panel;
        this.input = inputComando;
        this.output = consolaTextArea;
        this.inputString  = inputComandoString;
    }

    public void setNombreBaseDeDatos(String nombreBaseDeDatos) {
        this.nombreBaseDeDatos = nombreBaseDeDatos;
    }

      
    @Override
    public void run(){
        
        //En este if se verifica si la linea ingresada tiene inscribir, si es así procede a registrarse
        //El metodo que se llama para inscribir al servidor es inscribirServidor()
        
        if( inputString.trim().contains("inscribir")){
            
            output.setText( output.getText() + "Servidor Secundario > " + inputString + "\n");
            System.out.println("Voy a inscribir con la base: " + this.nombreBaseDeDatos);
            output.setText( output.getText() + inscribirServidor() + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            
        }
//        else if (inputString.trim().contains("basedd")) {
//            output.setText( output.getText() + "Servidor Secundario > " + inputString + "\n");
//            //this.nombreBaseDeDatos = inputString.substring(7);
//            setNombreBaseDeDatos(inputString.substring(7));
//            output.setText(output.getText() + "Base de Datos Elegida: " + nombreBaseDeDatos + "\n");
//            output.setLineWrap(true);
//            output.setWrapStyleWord(true);
//        }
        else if( inputString.trim().contains( "videos_descargando" ) ){
            
        }else if ( inputString.trim().contains( "videos_descargados" ) ){
            
        }else{
            
            output.setText( output.getText() + "Servidor Secundario > " + inputString + "\n");
            output.setText( output.getText() + "Servidor Secundario > Error en el comando" + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            
        }
        
    }

    /**
     * Metodo que devuelve un string de la respuesta del servidor central a la consola del cliente
     * @return Respuesta del servidor
     */
    private String inscribirServidor() {
    System.out.println("Servidor Secundario > LogicaCliente > Controladora > " + "Nombre de la base de datos: " + this.nombreBaseDeDatos);
        SocketConexionSecundario socket = new SocketConexionSecundario(this.nombreBaseDeDatos,this.ruta);
        return socket.inscribirServidor(datosServidorCentral[0] , 
                Integer.parseInt( datosServidorCentral[1]) ,Controladora.puertosServSecundario );
    }
}
