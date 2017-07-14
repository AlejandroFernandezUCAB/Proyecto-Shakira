/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.central;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author pedro
 */
public class Controladora {
    private JPanel consola;
    private JTextField input;
    private JTextArea output;

    public Controladora(JPanel consola,JTextField input, JTextArea output) {
        this.consola = consola;
        this.input = input;
        this.output = output;
    }
    
/**
 * Enviar información al servidor, por los momentos pura interfaz
 */    
    public void enviarInformacion (){
        
        String inputToString = input.getText().trim().toLowerCase().toString();
        if(inputToString.isEmpty() || inputToString == null || inputToString.equalsIgnoreCase("inserte comando aquí")){
            
            JOptionPane.showMessageDialog( consola , "El comando que introdujo es erroneo. \nIntente nuevamente" , "¡Error de comando!", JOptionPane.ERROR_MESSAGE);
            
        }else {
            
            output.setText( output.getText() + inputToString + "\n");
            output.setLineWrap(true);
            output.setWrapStyleWord(true);
            
        }
            
        
    }
        
}
