/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import LogicaCliente.ConsolaCliente;
import LogicaServidor.ConsolaServidor;
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
     * Se inicializan 2 hilos, uno para la consola del Cliente y otro para
     * la consola del servidor, de esa manera estará trabajando en modo :
     * Cliente-servidor
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         
       new ConsolaCliente().start();
       new ConsolaServidor().start();
       
    }
    
}
