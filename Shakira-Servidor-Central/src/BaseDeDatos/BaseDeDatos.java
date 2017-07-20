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
import java.util.StringTokenizer;
/**INSERT INTO CLIENTE(ipcliente, puertocmd, puertodata) VALUES(?, ?, ?)
 *
 * @author Alejandro Fernandez
 */
public class BaseDeDatos {

public static String driver = "org.postgresql.Driver";
//public static String connectString = "jdbc:postgresql://localhost:5432/Shakira-Servidor-Central";
public static String connectString = "jdbc:postgresql://localhost:5432/servidorCentral";
public static String user = "redes2";
public static String password = "redes2";
    

    /**
     * Constructor vacio para crear el objeto
     */
    public BaseDeDatos(){
            
    }
/**
 * Metodo que crea la conexion en la base de datos
     * @param ipYpuertos Direccion ip cliente
     * @param puertoEscucha Puerto escucha del cliente
     * @return devuelve 1 si se registró correctamente, si hay error 0
 */

    public String[] extraerIPyPuertos(String str){
        String[] campos = new String[3];
        int i = 0;
        StringTokenizer tokens = new StringTokenizer(str,"_");
        while(tokens.hasMoreTokens()){
             campos[i] = tokens.nextToken();
             //System.out.println(campos[i]);
             i++;
        }
        return campos;
    }
    
    public int agregarUsuarioBDD(String ipYpuertos){
        
        String[] campos = extraerIPyPuertos(ipYpuertos);
        
        String stm = "INSERT INTO CLIENTE(ipcliente, puertocmd, puertodata) VALUES(?, ?, ?)";
        PreparedStatement pst = null;
        Connection con=null;
        //Se abren las conexiones a la BDD y e guarda el usuario,
        if(verificarInscripcionUsuario(campos[0]) == false){
            try{
                Class.forName(driver);
                con = DriverManager.getConnection(connectString, user , password);
                pst = con.prepareStatement(stm);
                pst.setString(1, campos[0]);
                pst.setInt(2, Integer.parseInt( campos[1]) );    
                pst.setInt(3, Integer.parseInt( campos[2]) );                 
                pst.executeUpdate();

                } catch ( SQLException | ClassNotFoundException e ){

                    System.out.println("No se inscribio al usuario: " + campos[0]);
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
            System.out.println("Se inscribio al usuario: " + campos[0]);
            return 1;
        }else{
            System.out.println("El usuario: " + campos[0] + " ya existe");
            return 0;
        }
    }
    
    public boolean verificarInscripcionUsuario(String ip){
        boolean suiche = false;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ipcliente FROM cliente");

            while (rs.next()){
                
 //               System.out.println("Ip cliente (clase Base de Datos): " + rs.getString("ipcliente"));
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
}



