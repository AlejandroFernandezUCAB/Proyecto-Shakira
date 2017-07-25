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

/**
 *
 * @author Alejandro Fernandez
 */
public class BaseDeDatos {

    public static String driver = "org.postgresql.Driver";
//    public static String connectString = "jdbc:postgresql://localhost:5432/Shakira-Servidor-Secundario";
    public static String connectString = "jdbc:postgresql://localhost:5432/servidorSecundario";
    //public static String connectString = "jdbc:postgresql://localhost:5432/servidorCentral";
    public static String user = "redes2";
    public static String password = "redes2";
    public static String ruta = "C:/Prueba";
    
    /**
     * Metodo que se encarga de agarrar todos los videos que hay en el servidor
     * y retornarlos en un string
     * @return array de string con el nombre de los videos
     */
    public String[] nombreDeVideos() {
        String[] videos = new String[ cantidadDeVideos() ];
        System.out.println(videos.length);
        int i=0;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nombre FROM video");
            System.out.println("Servidor Secundario > Videos que se van a sincronizar:");
            while (rs.next()){
                
                System.out.println("Servidor Secundario > " + rs.getString("nombre") );
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
    /**
     * Este metodo me devuelve las rutas de cada video
     * @return Array con rutas de cadavideo
     * 
     */
    public String[] rutaDeVideos() {
        String[] rutas = new String[ cantidadDeVideos() ];
        int i = 0;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT rutaendisco FROM video");
            
            while (rs.next()){
                
                rutas[i] = rs.getString("rutaendisco");
                i++;
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
        
       return rutas;
       
    }
    
    /**
     * Se agrega el video a la bdd del servidor secundario
     * @param readLine Nombre del video
     * @param posicion posicion
     */
    public void agregarVideoServidorSecundario(String readLine, int posicion) {
        String stm = "INSERT INTO VIDEO VALUES( nextval('sec_id_video'), ?, ?, ?)";
        PreparedStatement pst = null;
        Connection con=null;
        //Se verifica que no haya un servidor con la misma Ip
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            pst = con.prepareStatement(stm);
            pst.setString(1, readLine );
            pst.setString(2, ruta + readLine);    
            pst.setInt(3, posicion);
            pst.executeUpdate();
        } catch ( SQLException | ClassNotFoundException e ){
           System.out.println(e.getMessage());        
           System.out.println("Servidor Central > No se inscribio el video: "+ readLine );
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
                }

        }
                System.out.println("Servidor Central > Se inscribio el video: " + readLine);
                
                
                     
        }
    
    /**
     * Devuelve la ruta de un video
     * @param nombreVideo Nombre del video
     * @return Ruta del video
     */
    public String rutaVideo(String nombreVideo) {
        String retorno=null;
        nombreVideo = nombreVideo.substring(10);
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT rutaendisco FROM video where nombre LIKE '"+nombreVideo+"'");
            
            while (rs.next()){
                
                retorno = rs.getString("rutaendisco");
                
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
        
        return retorno;
    }

    public void actualizarParte(String linea, String campo) {
        String stm = "UPDATE VIDEO set parteasignada="+ linea + " where nombre LIKE'" +campo+ "'" ;
        PreparedStatement pst = null;
        Connection con=null;
        //Se verifica que no haya un servidor con la misma Ip
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            pst = con.prepareStatement(stm);
            pst.executeUpdate();
        } catch ( SQLException | ClassNotFoundException e ){
           System.out.println(e.getMessage());        
           System.out.println("Servidor Central > No se inscribio el video: "+ campo);
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
                }

        }
                System.out.println("Servidor Central > Se inscribio el video: " + campo);
                
   
    }

    public void agregarVideoNuevo(String nombre, String ipLocal, String parte) {
        String stm = "INSERT INTO VIDEO VALUES( nextval('sec_id_video'),  ?,?,?)" ;
        PreparedStatement pst = null;
        Connection con=null;
        //Se verifica que no haya un servidor con la misma Ip
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            pst = con.prepareStatement(stm);
            pst.setString( 1, nombre);
            pst.setString( 2 , ipLocal);
            pst.setInt( 3, Integer.parseInt( parte ));
            pst.executeUpdate();
        } catch ( SQLException | ClassNotFoundException e ){
           System.out.println(e.getMessage());        
           System.out.println("Servidor Central > No se inscribio el video: "+ nombre);
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
                }

        }
                System.out.println("Servidor Central > Se inscribio el video: " + nombre);
                
                
    }
    
    /**
     *Devuelve la parte del video asignada a este servidor
     * @return parte
     */
    public int parteAsignadaDeVideo(String nombreVid){
        int parte = 0;
        String stm = "select parteasignada parte from video where nombre = ?";
        PreparedStatement pst = null;
        Connection con=null;
        //Se verifica que no haya un servidor con la misma Ip
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            pst = con.prepareStatement(stm);
            pst.setString(1, nombreVid );
                  
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                parte = rs.getInt("parte");
            }
        } catch ( SQLException | ClassNotFoundException e ){
           System.out.println(e.getMessage());        
           System.out.println("Servidor Central > No se pudo obtener la parte del video " + nombreVid );
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
                }

        }
        
       return parte;
        }
    
     public String rutaDeVideo(String nombreVid){
        String rutaVid = null;
        String stm = "select rutaendisco ruta from video where nombre = ?";
        PreparedStatement pst = null;
        Connection con=null;
        //Se verifica que no haya un servidor con la misma Ip
        try{
            Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            pst = con.prepareStatement(stm);
            pst.setString(1, nombreVid );
                  
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                rutaVid = rs.getString("ruta");
            }
        } catch ( SQLException | ClassNotFoundException e ){
           System.out.println(e.getMessage());        
           System.out.println("Servidor Central > No se pudo obtener la ruta del video " + nombreVid );
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
                }

        }
        
       return rutaVid;
        }
    
}
