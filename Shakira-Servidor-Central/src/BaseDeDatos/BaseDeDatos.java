/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseDeDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro Fernandez
 */
public class BaseDeDatos {

public static String driver = "org.postgresql.Driver";
public static String connectString = "jdbc:postgresql://localhost:5432/Shakira-Servidor-Central";
public static String user = "redes2";
public static String password = "redes2";
    

    /**
     * Constructor vacio para crear el objeto
     */
    public BaseDeDatos(){
            
    }
/**
 * Metodo que crea la conexion en la base de datos
     * @param direccionIp Direccion ip cliente
     * @param puertoEscucha Puerto escucha del cliente
     * @return devuelve 1 si se registró correctamente, si hay error 0
 */

    public int agregarUsuarioBDD(String direccionIp, int puertoEscucha){
        
        String stm = "INSERT INTO CLIENTE(ipcliente, puertoEscucha) VALUES(?, ?)";
        PreparedStatement pst = null;
        Connection con=null;
        //Se abren las conexiones a la BDD y e guarda el usuario,
        if(verificarInscripcionUsuario(direccionIp) == false){
            try{
                Class.forName(driver);
                con = DriverManager.getConnection(connectString, user , password);
                pst = con.prepareStatement(stm);
                pst.setString(1, direccionIp);
                pst.setInt(2, puertoEscucha);                    
                pst.executeUpdate();

                } catch ( SQLException | ClassNotFoundException e ){

                    System.out.println("Servidor Central > No se inscribio al usuario: " + direccionIp);
                    return 0;

                } finally {
                // Con el finally se cierran todas las conexiones los con, pst;
                    try {

                        if (pst != null) {
                            pst.close();
                        }
                        if (con != null) {
                            con.close();
                        }

                    } catch (SQLException ex) {

                        System.out.println(ex);                
                        return 1;
                    }

            }
            System.out.println("Servidor Central > Se inscribio al usuario: " + direccionIp);
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * Verifica si el cliente ya está registrado
     * @param ip direccion ip
     * @return regresa true si ya está registrado, false si no.
     */
    public boolean verificarInscripcionUsuario(String ip){
        boolean suiche = false;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ipcliente FROM cliente");

            while (rs.next()){
                
                System.out.println("Ip: " + rs.getString("ipcliente"));
                if(rs.getString("ipcliente").contains(ip)){
                    suiche = true;
                }
            }

                stmt.close();
                con.close();

            }catch ( Exception e ){
                 System.out.println(e.getMessage());
            }
        
            return suiche;
        }

    /**
     * Agrega a la bdd un servidor secundario
     * @param direccionIp 
     * @param puertoEscucha
     * @return regresa 0si no se pudo registrar, 1 si fue exitoso
     */
    public int agregarServidorBDD(String direccionIp, int puertoEscucha) {
        String stm = "INSERT INTO SERVIDOR (ipservidor, puertoEscucha) VALUES(?, ?)";
        PreparedStatement pst = null;
        Connection con=null;
        //Se verifica que no haya un servidor con la misma Ip
        if( verificarInscripcionDeServidor( direccionIp ) == false ){
            //Se verifica que no haya más de 3 servidores
            if( verificarCapacidadMaximaDeServidores( direccionIp ) == false){
                //Se abren las conexiones con la base de datos y se procede a guardar en la base de datos
                try{
                    Class.forName(driver);
                    con = DriverManager.getConnection(connectString, user , password);
                    pst = con.prepareStatement(stm);
                    pst.setString(1, direccionIp);
                    pst.setInt(2, puertoEscucha);                    
                    pst.executeUpdate();

                    } catch ( SQLException | ClassNotFoundException e ){

                        System.out.println("No se inscribio al servidor: " + direccionIp);
                        return 0;

                    } finally {
                    // Con el finally se cierran todas las conexiones los con, pst;
                        try {

                            if (pst != null) {
                                pst.close();
                            }
                            if (con != null) {
                                con.close();
                            }

                        } catch (SQLException ex) {

                            System.out.println(ex);                
                            return 1;
                        }

                }
                System.out.println("Servidor Central > Se inscribio al servidor: " + direccionIp);
                return 1;
                
            }else{ 
                
                System.out.println("Servidor Central > Maximo de servidores alcanzados");
                return 3;
                
            }

        }else{
            
            return 0;
            
        }
    }
    
    /**
     * Metodo que verifica si ya hay un servidor secundario registrado.
     * @param ip ip del servidor
     * @return True si ya hay un servior con esa ip, false si no lo hay
     */
    public boolean verificarInscripcionDeServidor(String ip){
         boolean suiche = false;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ipservidor FROM servidor");
            System.out.println("Ip de servidores actuales");
            while (rs.next()){
                
                System.out.println("Ip: " + rs.getString("ipservidor"));
                if(rs.getString("ipservidor").contains(ip)){
                    suiche = true;
                }
            }

                stmt.close();
                con.close();

            }catch ( Exception e ){
                 System.out.println(e.getMessage());
            }
        
            return suiche;
    }
    
    /**
     * Se verifica si no hay más de 3 servidores
     * @param direccionIp
     * @return true si hay más de 3 servidores, false si hay menos de 3
     */ 
    public boolean verificarCapacidadMaximaDeServidores(String direccionIp) {
        boolean suiche = false;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) servidores FROM servidor");
            System.out.println("Ip de servidores actuales");
            
            //Ciclo donde busco en el query la cantidad de servidores
            
            while (rs.next()){
                
                if(rs.getString("servidores").contains( direccionIp )){
                    suiche = true;
                }
                
            }

                stmt.close();
                con.close();

            }catch ( Exception e ){
                 System.out.println(e.getMessage());
            }
        
            return suiche;
    }
}



