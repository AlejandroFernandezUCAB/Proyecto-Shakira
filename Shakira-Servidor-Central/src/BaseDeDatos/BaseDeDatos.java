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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.StringTokenizer;
/**
 *
 * @author Alejandro Fernandez
 */
public class BaseDeDatos {

public static String driver = "org.postgresql.Driver";
public static String connectString = "jdbc:postgresql://localhost:5432/Shakira-Servidor-Central";
//public static String connectString = "jdbc:postgresql://localhost:5432/servidorCentral";
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
             System.out.println(campos[i]);
             i++;
        }
        return campos;
    }
    
    public int agregarUsuarioBDD(String ipYpuerto){
        
        String[] campos = extraerIPyPuertos(ipYpuerto);
        
        String stm = "INSERT INTO CLIENTE(ipcliente, puerto) VALUES(?, ?)";
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
    
    /**
     * Aqui se verifica que el cliente no haya estado inscrito
     * @param ip
     * @return 
     */
    public boolean verificarInscripcionUsuario(String ip){
        boolean suiche = false;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ipcliente FROM cliente");

            while (rs.next()){
                
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
    public int agregarServidorBDD(String ip) {
        String stm = "INSERT INTO SERVIDOR (ipservidor, puertocmd, puertodata, estado) VALUES(?, ?, ?, 0)";
        PreparedStatement pst = null;
        Connection con=null;
        String[] campos = extraerIPyPuertos( ip );
        //Se verifica que no haya un servidor con la misma Ip
        if( verificarInscripcionDeServidor( campos[0] ) == false ){
            //Se verifica que no haya más de 3 servidores
            if( verificarCapacidadMaximaDeServidores( campos[0] ) == false){
                //Se abren las conexiones con la base de datos y se procede a guardar en la base de datos
                try{
                    Class.forName(driver);
                    con = DriverManager.getConnection(connectString, user , password);
                    pst = con.prepareStatement(stm);
                    pst.setString(1, campos[0]);
                    pst.setInt(2, Integer.parseInt( campos[1]) );                    
                    pst.setInt(3, Integer.parseInt( campos[2]) );                    
                    pst.executeUpdate();

                    } catch ( SQLException | ClassNotFoundException e ){
                        System.out.println(e.getMessage());        
                        System.out.println("Servidor Central > No se inscribio al servidor: " + campos[0]);
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
                System.out.println("Servidor Central > Se inscribio al servidor: " + campos[0]);
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
     * Metodo que agrega a la base de datos el video
     * @param ipServidor Ip Del dueño del video
     * @param video Nombre dle video a guardar
     * @return si es true se guardó correctamente y false hubo error
     */
    public boolean agregarVideoSincronizacion(String ipServidor ,String video){        
        String stm = "INSERT INTO VIDEO(id_video, nombre) VALUES( nextval('sec_id_video') ,?)";
        PreparedStatement pst = null;
        Connection con=null;
        //Se abren las conexiones a la BDD y se guarda el video
        
            try{
                Class.forName(driver);
                con = DriverManager.getConnection(connectString, user , password);
                pst = con.prepareStatement(stm);
                pst.setString(1, video);
                pst.executeUpdate();
                stm="INSERT INTO VIDEOS_SERVIDOR VALUES(true,true,true,"+ idVideo(video)+",'"+ipServidor+"')";
                pst = con.prepareStatement(stm);
                pst.executeUpdate();
                
                } catch ( SQLException | ClassNotFoundException e ){
                    System.out.println(e.getMessage());
                    System.err.println("Servidor Central > No se inscribio el video: " + video);
                    return false;

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

                        System.err.println(ex);                
                        return true;
                    }

            }
           
            return true;
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
            System.out.println("Servidor Central > Ip de servidores actuales");
            while (rs.next()){
                
                System.out.println("Servidor Central > Ip: " + rs.getString("ipservidor"));
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
            
            //Ciclo donde busco en el query la cantidad de servidores
            while (rs.next()){
                
                if(rs.getString("servidores").contains( "3" )){
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
     * Verifica si ya hay 3 servidores con el estado en 2 (Si es 2 es que ya enviaron videos)
     * @return true si a hay 3 servidores, false si hay menos
     */
    public boolean verificarServidores() {
        boolean suiche = false;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) servidores FROM servidor WHERE estado=1");
            
            //Ciclo donde busco en el query la cantidad de servidores
            while (rs.next()){
                if(rs.getString("servidores").contains( "3" )){ //Debería ser 3, pero esto comienza a contar desde 0
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
     * Metodo que retorna la cantidad de videos alojados en el servidor
     * @return cantidad de videos alojados en el servidor
     */
    public int cantidadVideosAlojados() {
        int cantidad = 0; 
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) cantidad from video");
            
            //Ciclo donde busco en el query la cantidad de servidores
            while (rs.next()){
                
                cantidad = rs.getInt("cantidad");
            }
            

                stmt.close();
                con.close();
                
            }catch ( Exception e ){
                
                 System.out.println(e.getMessage());
            }
        
        return cantidad;
    }
    /**
     *Metodo que consulta la BD para obtener la IP de los servidores secundarios
     * @return Lista de Strings con las ip de los servidores
     */
    public List<String> listaServidoresSecundarios() {
        //declaro las variables
        List<String> ListaDeIP = new ArrayList<String>();
        int status = 0;        
        PreparedStatement pst = null;
        Connection con=null;
        
        //armo el string con el query
        String stm = "SELECT ipservidor ip FROM servidor";
       
        try{
        Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            //preparo el comando de sql
            pst = con.prepareStatement(stm);
                //Statement stmt = con.createStatement();
            ResultSet rs;
            //ejecuto el Query
            rs = pst.executeQuery();
            
            //recorro el conjunt y agrego los elementos a la lista
            while (rs.next()){
                ListaDeIP.add(rs.getString("ip"));
            }
            //cierro la conexion
                con.close();
                
                
        }catch ( SQLException | ClassNotFoundException e ){
                
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

                    }catch (SQLException ex) {

                        System.out.println(ex); 
                        return null;
                    }
            }
        return ListaDeIP;
    }
    
    /*
    public int puertoDeIP(String ipCliente){
        //declaro las variables
        int puerto = 0;             
        PreparedStatement pst = null;
        Connection con=null;
        //armo el string con el query  
        String stm = "SELECT puerto from cliente where ipcliente = ?";
        try{
        Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            //preparo el comando de sql
            pst = con.prepareStatement(stm);
            pst.setString(1, ipCliente);
            //Statement stmt = con.createStatement();
            ResultSet rs;
            //ejecuto el query
            rs = pst.executeQuery();
            //guardo los resultados
            if(rs.next()){
                puerto = rs.getInt("puerto");
            }
        }
        catch ( SQLException | ClassNotFoundException e ){
                
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

                    }catch (SQLException ex) {

                        System.out.println(ex); 
                    }
            }
        
    return puerto;
    }
    */
    
    
    /**
     *Metodo que consulta la BD para obtener los puertos correspondientes a una IP
     * @param ipServidor
     * @return puertos del servidor secundario, data = 0, cmd = 1
     */
    public int[] listaDePuertos(String ipServidor){
        //declaro las variables
        int[] puertos = new int[2];             
        PreparedStatement pst = null;
        Connection con=null;
        //armo el string con el query  
        String stm = "SELECT puertocmd as cmd, puertodata as data from servidor where ipservidor = ?";
        try{
        Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            //preparo el comando de sql
            pst = con.prepareStatement(stm);
            pst.setString(1, ipServidor);
            //Statement stmt = con.createStatement();
            ResultSet rs;
            //ejecuto el query
            rs = pst.executeQuery();
            //guardo los resultados
        if(rs.next()){
            puertos[0] = Integer.parseInt(rs.getString("cmd"));
            puertos[1] = Integer.parseInt(rs.getString("data"));}
        }
        catch ( SQLException | ClassNotFoundException e ){
                
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

                    }catch (SQLException ex) {

                        System.out.println(ex); 
                    }
            }
        
    return puertos;
    }
    
    /**
     *Metodo que consulta la BD para obtener el id de un video dado
     * @param nombreVid
     * @return 0 si hubo un error, de lo contrario devuelve un entero que es el id del video
     */
    public int idVideo(String nombreVid) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //declaro las variables
        int idVid = 0;          
        PreparedStatement pst = null;
        Connection con=null;
        //armo el string con el query  
        String stm = "select id_video id from video where nombre = ?";
                
        try{
                    Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            //preparo el comando de sql
            pst = con.prepareStatement(stm);
            pst.setString(1, nombreVid);
                //Statement stmt = con.createStatement();
            ResultSet rs;
            //ejecuto el query
            rs = pst.executeQuery();
            //guardo el resultado
            if (rs.next()) {
                idVid = rs.getInt("id");
            }
            
        }
        catch ( SQLException | ClassNotFoundException e ){
                
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

                    }catch (SQLException ex) {

                        System.out.println(ex); 
                    }
            }
        
        return idVid;
    }
    
    /**
     * Que videos hay alojados
     * @param cantidad cantidad de videos
     * @return 
     */
    public String[] videosAlojados( int cantidad ) {
        String[] videos = new String[ cantidad ];
        int i = 0;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nombre from video");
            
            //Ciclo donde busco en el query la cantidad de servidores
            while (rs.next()){
                
                videos[i] = rs.getString( "nombre" );
                i++;
                
            }
            

                stmt.close();
                con.close();
                
            }catch ( Exception e ){
                
                 System.out.println(e.getMessage());
            }
        
        return videos;
        
    }

    /**
     * Actualiza el estado del servidor secundario a listo
     * @param hostAddress DIreccion ip a actualizar
     */
    public void actualizarEstadoServidorSecundario(String hostAddress) {
        String stm = "UPDATE SERVIDOR SET estado=1 WHERE ipservidor='"+ hostAddress+"'";
        PreparedStatement pst = null;
        Connection con=null;
        //Se abren las conexiones a la BDD y se guarda el video
        
            try{
                Class.forName(driver);
                con = DriverManager.getConnection(connectString, user , password);
                pst = con.prepareStatement(stm);            
                pst.executeUpdate();

                } catch ( SQLException | ClassNotFoundException e ){

                    System.err.println("Servidor Central > No se actualizó al servidor: " + hostAddress);

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

                        System.err.println(ex);                

                    }

            }

    }
    /**
     * Metodo que extrae el video, la ip y el puerto por donde se va a descargar
     * @param ipServidor ip del servidor actual
     * @return vector de string
     */
    public String[] videoIpPuerto(String ipServidor) {
        String[] videos = null;
        int i = 0;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) cantidad from video, videos_servidor,servidor where servidor=ipservidor and videofk = id_video and ipservidor != '"+ipServidor+"'");
            
            //Ciclo donde busco en el query la cantidad de videos para inicializar la variable
            while (rs.next()){
                videos = new String[ rs.getInt("cantidad")];
                System.out.println(rs.getString("cantidad"));
            }
            stmt.close();
            con.close();
            Class.forName(driver);
            con = DriverManager.getConnection(connectString, user , password);
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT v.nombre nombre,s.ipservidor servidor, s.puertocmd puerto " +
                                    "FROM video as v, videos_servidor as vs, servidor as s "  +
                                    "WHERE v.id_video = vs.videofk AND vs.servidor = s.ipservidor AND vs.servidor NOT LIKE '"+ipServidor+"' AND vs.servidor = s.ipservidor ");
            
            //Se agregan al query las diferentes direcciones separadas por un _
             while (rs.next()){
                videos[i] = rs.getString("nombre")+"_"+rs.getString("servidor")+"_"+rs.getString("puerto");
                System.out.println(videos[i]);
                i++;
            }
                stmt.close();
                con.close();
                
            }catch ( Exception e ){
                
                 System.out.println(e.getMessage());
            }
        
        return videos;
        
    }

    public String queParteMeToco(String str) {
        str = str.substring(13);
        boolean[] parte = new boolean[1]; // si es 01 guardará como 3era parte, si es 11 guardará como 2da parte
        int id = idVideo(str);
        int i=1;
        try{
            Class.forName(driver);
            Connection con = DriverManager.getConnection(connectString, user , password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select parte1, parte2, parte3 from videos_servidor where "+id+"=videofk");
            
            //Ciclo donde busco en el query la cantidad de videos para inicializar la variable
            while (rs.next()){
                
                if( rs.getBoolean("parte1") == false && rs.getBoolean("parte2") == true && rs.getBoolean("parte4") == false ){
                    parte[0]=false;
                    parte[1]=true;
                }else if(rs.getBoolean("parte1") == true && rs.getBoolean("parte2") == true && rs.getBoolean("parte4") == true){
                    parte[0]=true;
                    parte[1]=true;
                }
            }
            stmt.close();
            con.close();
            
            if(parte[0] == false && parte[1]== true){
                
            }else if (parte[0] ==true && parte[1]==true){
                
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "hola";
    }
}
