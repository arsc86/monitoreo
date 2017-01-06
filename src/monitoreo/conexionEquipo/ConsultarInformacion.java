/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.conexionEquipo;


import java.net.InetAddress;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import monitoreo.conexiondb.ConexionDataBase;
import monitoreo.conexiondb.ConsultasDataBase;
import monitoreo.entidades.InfoEquipo;
import monitoreo.entidades.InfoEquipoHistorial;
import monitoreo.entidades.Utils;
import monitoreo.entidades.ValoresEjecucion;

/**
 *
 * @author arsuarez
 */
public class ConsultarInformacion {
    
    private static Connection connection;
    private static ConsultasDataBase consultasDB = new ConsultasDataBase();
    private static Utils utils;
    private static Ssh ssh = new Ssh();
    
    public static void consultarComando(ValoresEjecucion valoresEjecucion) throws Exception{
        
        switch(valoresEjecucion.getProceso())
        {
            case "ping":
                ejecutarPing();
                break;
                
            case "getConfig":
                break;
                           
                
        }
        
    }
    
    private static void ejecutarPing() throws SQLException, ClassNotFoundException
    {
        connection = ConexionDataBase.obtenerConexionBaseMySql();
        
        if(connection != null)
        {
            ArrayList<InfoEquipo> equiposList = consultasDB.getInformacionEquipos(0,connection);
                        
            for(final InfoEquipo equipo : equiposList)
            {
                Runnable runnableEjecucion  = new Runnable() {

                    @Override
                    public void run() {
                                                
                        try
                        {                                                    
                            boolean hacePing = ssh.hostEsAlcanzable(equipo.getIp());
                            
                            if(!hacePing)
                            {            
                                equipo.setStatus("DOWN");
                                
                                //Se actualiza status en info equipo e historial en caso de fallar
                                InfoEquipoHistorial historial = new InfoEquipoHistorial();
                                historial.setEquipo(equipo);
                                historial.setEstado("Activo");
                                historial.setObservacion("Equipo no es alcanzable en la red ( fallo al hacer PING )");
                                historial.setUsrCreacion("monitoreo");
                                historial.setFeCreacion(new java.sql.Timestamp(System.currentTimeMillis()));
                                
                                connection = ConexionDataBase.obtenerConexionBaseMySql();
                                consultasDB.crearHistorialEquipo(historial, connection);
                                consultasDB.updateEquipo(equipo, connection);                                
                            }
                            else
                            {
                                if(equipo.getStatus().equalsIgnoreCase("DOWN"))
                                {
                                    equipo.setStatus("UP");
                                    connection = ConexionDataBase.obtenerConexionBaseMySql();
                                    consultasDB.updateEquipo(equipo, connection);
                                }
                            }
                            
                            connection.close();
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }                                                
                    }
                };
                
                Thread ejecucionPing = new Thread(runnableEjecucion);
                ejecucionPing.start();
            }
        }
    }
    
}
