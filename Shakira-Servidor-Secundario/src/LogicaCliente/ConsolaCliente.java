/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LogicaCliente;

import InterfazCliente.ConsolaShakira;

/**
 * Esta clase se usará para correr un hilo para el cliente y así poder tener 
 * @author Alejandro Fernandez
 */
public class ConsolaCliente extends Thread{
    
    @Override
    public void run(){
        
        ConsolaShakira consola = new ConsolaShakira();
        consola.show();
        
    }
    
}
