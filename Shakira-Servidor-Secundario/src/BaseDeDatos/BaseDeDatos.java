/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseDeDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Alejandro Fernandez
 */
public class BaseDeDatos {

    public static String driver = "org.postgresql.Driver";
    public static String connectString = "jdbc:postgresql://localhost:5432/Shakira-Servidor-Secundario";
    //public static String connectString = "jdbc:postgresql://localhost:5432/servidorCentral";
    public static String user = "redes2";
    public static String password = "redes2";
    
    /**
     * Metodo que se encarga de agarrar todos los videos que hay en el servidor
     * y retornarlos en un string
     * @return array de string con el nombre de los videos
     */
    public String[] nombreDeVideos() {
        String[] videos = new String[ cantidadDeVideos() ];
        System.out.println(videos.length);
        int i=1;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nombre FROM video");
            System.out.println("Servidor Secundario > Videos que se van a sincronizar:");
            while (rs.next()){
                
                System.out.println("Servidor Secundario > " + rs.getString("nombre"));
                videos[i] = rs.getString("nombre");
                i++;
                
            }
            
            con.close();
            stmt.close();
            
            }catch ( SQLException e ){
                System.out.println("Error en la ejecución del SQL");
            }catch ( ClassNotFoundException e){
                System.out.println("Error, no se encuentra la clase Driver");
            }catch ( Exception e){
                System.out.println(e.getMessage());
            }         
            return videos;
    }
    
    /**
     * Con este metodo extraigo cuantos videos hay en el servidor y así poder crear el string
     * @return cantidad de video alojados en el servidor
     */
    private int cantidadDeVideos() {
        int cantidad = 0;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) cantidad FROM video");
            
            while (rs.next()){
                
                cantidad = rs.getInt("cantidad");
                
            }
            
            con.close();
            stmt.close();
            
            }catch ( SQLException e ){
                System.out.println("Error en la ejecución del SQL buscando la cantidad");
            }catch ( ClassNotFoundException e){
                System.out.println("Error, no se encuentra la clase Driver");
            }catch ( Exception e){
                System.out.println(e.getMessage());
            }        
        
       return cantidad;
       
    }
    
}
