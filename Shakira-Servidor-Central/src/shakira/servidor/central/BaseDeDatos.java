/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.central;

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
        //Se abren las conexiones a la BDD y e guarda el usuario, aun no se verifica si se guardo
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            pst = con.prepareStatement(stm);
            pst.setString(1, direccionIp);
            pst.setInt(2, puertoEscucha);                    
            pst.executeUpdate();
            
            } catch ( SQLException | ClassNotFoundException e ){

                System.out.println(e.getMessage());
                
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
                    return 0;
                }
                
                return 1;
        }
           
    }
}



