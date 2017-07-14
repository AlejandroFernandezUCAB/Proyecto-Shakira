/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shakira.servidor.central;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alejandro Fernandez
 */
public class BaseDeDatos extends Exception {

public static String driver = "org.postgresql.Driver";
public static String connectString = "jdbc:postgresql://localhost:5432/Shakira-Servidor-Central";
public static String user = "redes2";
public static String password = "redes2";

/**
 * Metodo que crea la conexion en la base de datos
 */
    public void abrirConexion() {
        
        try{

            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            } catch ( SQLException | ClassNotFoundException e ){

                System.out.println(e.getMessage());
            }
    }

    public int agregarUsuarioBDD(String direccionIp, int puertoEscucha){
        u
    }
}



