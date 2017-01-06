/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.conexiondb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import monitoreo.entidades.DatosConexionDataBase;

/**
 *
 * @author arsuarez
 */
public class ConexionDataBase {
    
    private static Connection connection;   
    
    public static Connection obtenerConexionBaseMySql() 
            throws SQLException, ClassNotFoundException {

        try 
        {
            DatosConexionDataBase con = new DatosConexionDataBase();
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+ con.getHost()+ ":"+con.getPort()+"/" + con.getDataBase(), con.getUsername(), con.getPassword());                        
        } 
        catch (ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
        
        return connection;

    }
    
    public static void closeConnection() throws SQLException
    {
        connection.close();
    }
        
    
   
}
