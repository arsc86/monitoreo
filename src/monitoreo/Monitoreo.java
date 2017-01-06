/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import monitoreo.conexionEquipo.ConsultarInformacion;
import monitoreo.conexionEquipo.EjecutarComandos;
import monitoreo.conexiondb.ConexionDataBase;
import monitoreo.entidades.ValoresEjecucion;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author arsuarez
 */
public class Monitoreo {
        
    static Connection connection;
    static ConexionDataBase conexionDataBase;    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException, Exception{
        // TODO code application logic here
        
//        String tipoOperacion  = args[0];//ejecucion - consultas
//        String proceso        = args[1];
//        String idScript       = args[2];
//        String idsEquipo      = args[3];//Todos o id
//        String tipo           = args[4];
//        String idJob          = args[5]; 
        
        String tipoOperacion  = "CONSULTAR";//ejecucion - consultas
        String proceso        = "ping";
        String idScript       = null;
        String idsEquipo      = "0";//Todos o id
        String tipo           = "";
        String idJob          = "";         
        
        ValoresEjecucion valoresEjecucion = new ValoresEjecucion();
        valoresEjecucion.setComando(idScript);
        valoresEjecucion.setIdsEquipo(idsEquipo);
        valoresEjecucion.setProceso(proceso);
        valoresEjecucion.setValores(tipo);
        valoresEjecucion.setJob(idJob);
        
        switch(tipoOperacion)
        {
            case "EJECUTAR":
                EjecutarComandos.ejecutarComando(valoresEjecucion);
                break;
                
            case "CONSULTAR":
                ConsultarInformacion.consultarComando(valoresEjecucion);
                break;  
                
            case "EJECUTAR_JOB":
                break;
                
            case "EJECUTAR_JOB_PROGRAMADO":
                break;
        }
        
        
        connection = ConexionDataBase.obtenerConexionBaseMySql();
        
        ConexionDataBase.closeConnection();
        
    }
    
    
    
}
