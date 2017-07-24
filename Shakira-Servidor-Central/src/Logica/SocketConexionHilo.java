/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import BaseDeDatos.BaseDeDatos;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Alejandro Fernandez
 */
public class SocketConexionHilo extends Thread{
       
    private Socket ss;
    private int counter;
    
    public SocketConexionHilo( Socket i, int c){
        ss = i;
        counter = c;
    }
    
    @Override
    public void run() {
        
        BufferedReader entrada = null;
        PrintWriter salida = null;
        //System.out.println("Escuchando: al socket" + ss);
        int suiche;
        try {
            
          //Establece el canal de entrada
          entrada = new BufferedReader(new InputStreamReader(ss.getInputStream()));
          // Establece canal de salida
          salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ss.getOutputStream())),true);
          
          //En este while se recibe la información del cliente y se procede a guardar en la base de datos
          //Además se le responde al cliente 
            
          
          
          while (true) {  
            String str = entrada.readLine();
              System.out.println(str);
            //-------INSCRIPCION----------
            
            if( str.contains("inscribirU")){
                System.out.println("---------------Inscribiendo Usuario--------------");
                suiche = inscribirUsuario(str);                                
                if( suiche == 1){
                   salida.println( "Servidor Central> Inscrito correctamente");                   
                }else{
                   salida.println( "Servidor Central> Ya ud se ha registrado");
                }
                System.out.println("---------------FIN proceso--------------");
                
            }else if( str.trim().contains("inscribirS")){
                System.out.println("---------------Inscribiendo Servidor Secundario--------------");
                suiche = inscribirServidorSecundario(str);                                
                if( suiche == 1){
                    
                   salida.println( "Servidor Central > Servidor inscrito correctamente");                   
                   
                }else if (suiche == 0){
                    
                   salida.println( "Servidor Central > Ya ud se ha registrado");
                   
                }else{
                   salida.println( "Servidor Central > Ya hay 3 servidores registrados");
                }
                System.out.println("---------------FIN proceso--------------");
            }
            
            //-------DESCARGA----------
            
            else if ( str.trim().contains("descarga")) {
                System.out.println("---------------DESCARGA--------------");
                String ipCliente = ss.getInetAddress().toString().substring(1);
                //int puertoCliente = this.puertoAsociadoIP(ipCliente);
                System.out.println("entrada: " + str);
                //System.out.println("Direccion IP del cliente: " + ipCliente + " : " + puertoCliente);
                System.out.println("Direccion IP del cliente: " + ipCliente );
                //verifico si el usuario esta inscrito
                    //verifico que el nombre del video existe
                String nombreVid = this.extraerNombreVideo(str);
                
                int idVideo = obtenerIDVideo(nombreVid);
                //
                if (this.verificarInscripcionUsuario(ipCliente)  == true    && 
                    idVideo != 0) 
                {
                    System.out.println("usuario Inscrito");
                    //salida.println( "Servidor Central > Usuario registrado!....Peticion de descarga aprobada");
                   
                    //ya habiendo verificado que el cliente existe y tengo el video
                    //le digo a los servidores secundarios que se lo envien...
                    List<String> servidores = this.obtenerListaDeServidores();
                    //enviarVideo_ipCliente_puertoCliente_idVideo_ipServidor)
                    System.out.println("id del Video: " + idVideo);
                    System.out.println("Ip servidores secundarios:");
                    
                    String infoServidores = "";
                    for (Iterator<String> i = servidores.iterator(); i.hasNext();) {
                        String ip = i.next();
                        int puerto = puertosAsociados(ip)[0];
                        //System.out.println(i.next());
                        System.out.println(ip);
                        infoServidores += ip + "_" + puerto;
                        
                        if (i.hasNext()) {
                            infoServidores += "_";
                        }
                    }
                     salida.println(infoServidores);
                    
                }
                else{
                    salida.println("Video no existe o usuario no registrado...");
                }
                System.out.println("-------------FIN PROCESO DESCARGA-----------");
              }
            break;
          }
          
        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        }  
        
        try{
            salida.close();
            entrada.close();
            ss.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    /*
    private String enviarVideo(String ipCliente, int puertoCliente, String ipServidor, int puertoServidor, String nombreVid){
        //Sub-Socket de Prueba    
                   
                    String resultado = null;
                    BufferedReader entradaSubSocket = null;
                    PrintWriter salidaSubSocket = null;
                    Socket subSocket = null;
                    try{
                       
                       subSocket = new Socket(ipServidor, puertoServidor);
                       System.out.println("Se inicializa el Sub-socket:" + subSocket);
                       entradaSubSocket = new BufferedReader(new InputStreamReader(subSocket.getInputStream()));
                       // Obtenemos el canal de salida
                       salidaSubSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(subSocket.getOutputStream())),true);
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                    
                    
                    BufferedReader subStdIn = new BufferedReader(new InputStreamReader(System.in));
                    String subLinea=null;
                    
                    try {
                        while (true) {
                          // Leo la entrada del usuario
                          String subStr = "enviarVideo_"+ipCliente + "_" +puertoCliente + "_" + nombreVid;
                          // La envia al servidor
                          salidaSubSocket.println(subStr);
                          System.out.println("Se envio al sub-socket: "+ subStr);
                          // Envía a la salida estándar la respuesta del servidor
                          subLinea = entradaSubSocket.readLine();
                          
                          resultado = "Respuesta servidor: " + subLinea;    
                          
                          break;
                        }
                      } catch (Exception e) {
                          resultado = "Problemas de conexión con el servidor secundario, intente más tarde";
                      } 
                    
                    try{
                            // Libera recursos
                            salidaSubSocket.close();
                            entradaSubSocket.close();
                            subStdIn.close();
                            subSocket.close();
                        }
                    catch (IOException e) {
                            System.out.println("IOException: " + e.getMessage());
                            resultado = "Error cerrando conexion del socket hacia el servidor secundario";
                            }      
                     
                        //fin sub socket de prueba
                   return resultado;
    }
*/
    /**
     * Metodo encargado de inscribir el usuario en la base de datos, el metodo quita la paabra inscribir
     * @param entrada 
     * @return si devuelve 1 significa que se guardo correctamente, si devuelve 0 ya estaba registrado
     */
    private int inscribirUsuario(String entrada) {
        
            entrada = entrada.substring(12);
            BaseDeDatos bdd = new BaseDeDatos();
            return bdd.agregarUsuarioBDD(entrada);
            
    }

    /**
     * Metodo que inscribe al servidor secundario en la BDD
     * @param entrada ip del servidor
     * @return Regresa 1 si fue exitoso, 0 si ya estaba o hubo algún error y otro si ya hay 3 servidores registrados
     */
    private int inscribirServidorSecundario(String entrada) {
        
            entrada = entrada.substring(12);
            BaseDeDatos bdd = new BaseDeDatos();
            return bdd.agregarServidorBDD(entrada);
            
    }
    
    
    private boolean verificarInscripcionUsuario(String entrada){
        BaseDeDatos bdd = new BaseDeDatos();
        return bdd.verificarInscripcionUsuario(entrada);
    }
    
    
    private String extraerNombreVideo(String mensaje){
        return mensaje.substring(9);
    }
    
    
    private List<String> obtenerListaDeServidores(){
        BaseDeDatos bdd = new BaseDeDatos();
        return bdd.listaServidoresSecundarios();
    }
    
    private int[] puertosAsociados(String IpServidor){
        BaseDeDatos bdd = new BaseDeDatos();
        return bdd.listaDePuertos(IpServidor);
    }
    /*
    private int puertoAsociadoIP(String ipCliente){
        BaseDeDatos bdd = new BaseDeDatos();
        return bdd.puertoDeIP(ipCliente);
    }
    */
    private int obtenerIDVideo(String nombreVid) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        BaseDeDatos bdd = new BaseDeDatos();
        return bdd.idVideo(nombreVid);
    }

    
}
