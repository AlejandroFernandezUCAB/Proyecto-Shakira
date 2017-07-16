/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.secundario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author pedro
 */
public class ShakiraServidorSecundario {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         
        String comando;
        BufferedReader br ;
        
        while(true){  
            try{
                comando = "";
                br = null;
                br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Servidor Secundario > ");
                comando = br.readLine();
                comando = comando.trim();
                new Controladora( comando ).start();
                
            }catch(IOException e){

                System.out.println(e.getMessage());

            }
        }
    }
    
}
