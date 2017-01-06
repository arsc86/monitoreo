/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.entidades;

import java.util.Properties;

/**
 *
 * @author arsuarez
 */
public class DatosConexionDataBase {
    
    private String host;
    private String username;
    private String password;
    private String dataBase;
    private String port;
    
    public DatosConexionDataBase()
    {
        Utils utils = new Utils();
        Properties properties = utils.getConfiguration();
        if(properties!=null)
        {
            this.host     = properties.getProperty("monitoreo.conexion.host");
            this.username = properties.getProperty("monitoreo.conexion.username");
            this.password = properties.getProperty("monitoreo.conexion.password");
            this.dataBase = properties.getProperty("monitoreo.conexion.dataBase");
            this.port     = properties.getProperty("monitoreo.conexion.port");
        }
    }            

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    
    
    
    
}
