/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.conexiondb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import monitoreo.entidades.InfoAcceso;
import monitoreo.entidades.InfoEquipo;
import monitoreo.entidades.InfoEquipoHistorial;
import monitoreo.entidades.InfoScript;

/**
 *
 * @author arsuarez
 */
public class ConsultasDataBase {
    
    static Connection connection;
    
    public void setConnection(Connection conn){
        connection = conn;
    }
            
    /**
     * Metodo para obtener la informacion de que equipos y script a ser ejecutado
     * 
     * @param list
     * @param idScript
     * @return 
     */
    public ArrayList<InfoAcceso> getInformacionEjecucion(ArrayList<Integer> list , int idScript, Connection connection)
    {
        String params = "";
        for(Object o : list)
        {
            params += params + "?,";
        }
        
        params = params.substring(0, params.length()-1); 
        
        String sql = "select \n" +
                    "e.ID_EQUIPO, \n" +
                    "e.IP, \n" +
                    "a.USUARIO_ACCESO, \n" +
                    "a.CLAVE_ACCESO,\n" +
                    "(select CONVERT(script USING utf8) SCRIPT "
                    + "from info_script where id_script = ?) SCRIPT\n" +
                    "from \n" +
                    "info_equipo e,\n" +
                    "admi_acceso a\n" +
                    "where\n" +
                    "e.id_equipo = ("+params+") and\n" +
                    "e.acceso_id = a.id_acceso";       
      
        try{
                        
            PreparedStatement ps = connection.prepareStatement(sql); 
            
            ps.setInt(1, idScript);
            
            int cont = 1;
            for(Object o : list)
            {                
                ps.setObject(cont, o);
                cont++;
            }              
            
            ArrayList<InfoAcceso> accesoList = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            
            InfoAcceso acceso;
            
            while(rs.next()){
                
                acceso = new InfoAcceso();
                acceso.setIdEquipo(rs.getString("ID_EQUIPO"));
                acceso.setIpEquipo(rs.getString("IP"));
                acceso.setUsuarioAcceso(rs.getString("USUARIO_ACCESO"));
                acceso.setPasswordAcceso(rs.getString("CLAVE_ACCESO"));
                acceso.setScript(rs.getString("SCRIPT"));
                accesoList.add(acceso);
                
            }
            
            rs.close();
            ps.close();    
            
            connection.close();
            
            return accesoList;
                            
        } catch (SQLException e) {            
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }  
        
        return null;
    }
    
    public ArrayList<InfoEquipo> getInformacionEquipos(int idEquipo, Connection connection)
    {
        String where = "";
        
        if(idEquipo != 0)
        {
            where = " and e.id_equipo = ? ";
        }
        
        
        String sql = "select \n" +
                    "e.ID_EQUIPO, \n" +
                    "e.IP, \n" +
                    "e.TIPO, "+
                    "a.USUARIO_ACCESO, \n" +
                    "a.CLAVE_ACCESO, \n" +                   
                    "e.STATUS \n" +                   
                    "from \n" +
                    "info_equipo e,\n" +
                    "admi_acceso a\n" +
                    "where \n" +                    
                    "e.acceso_id = a.id_acceso "
                    +where+
                    " and e.estado    = 'Activo' ";       
      
        try{
                        
            PreparedStatement ps = connection.prepareStatement(sql); 
            
            if(idEquipo != 0)
            {   
                ps.setInt(1, idEquipo);
            }                            
            
            ArrayList<InfoEquipo> equipoList = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            
            InfoEquipo equipo;
            InfoAcceso acceso;
            
            while(rs.next()){
                
                equipo = new InfoEquipo();
                equipo.setId(Integer.parseInt(rs.getString("ID_EQUIPO")));
                equipo.setIp(rs.getString("IP"));
                equipo.setTipo(rs.getString("TIPO"));
                equipo.setStatus(rs.getString("STATUS"));
                
                acceso = new InfoAcceso();
                acceso.setUsuarioAcceso(rs.getString("USUARIO_ACCESO"));
                acceso.setPasswordAcceso(rs.getString("CLAVE_ACCESO"));
               
                equipo.setAcceso(acceso);
                equipoList.add(equipo);
                
            }
            
            rs.close();
            ps.close();    
            
            connection.close();
            
            return equipoList;
                            
        } catch (SQLException e) {            
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }  
        
        return null;
    }
    
    public String getScript(String nombre)
    {
        
        
        String sql = "select CONVERT(script USING utf8) SCRIPT, "
                     +" FROM "
                + "INFO_SCRIPT where estado <> 'Eliminado' and nombre_script = ?";       
      
        try{
                        
            PreparedStatement ps = connection.prepareStatement(sql); 
                       
            ResultSet rs = ps.executeQuery();
            
            String script = null;
            
            
            if(rs.next()){
       
                script = rs.getString("SCRIPT");
                
            }
            
            rs.close();
            ps.close();    
            
            connection.close();
            
            return script;
                            
        } catch (SQLException e) {            
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }  
        
        return null;
    }
    
    public void crearHistorialEquipo(InfoEquipoHistorial historial,Connection connection)
    {
        
        String sql = "insert into info_equipo_historial(equipo_id,observacion,estado,fe_creacion,usr_creacion) "
                + "values (?,?,?,?,?)";       
      
        try{
                        
            PreparedStatement ps = connection.prepareStatement(sql); 
            
            ps.setInt(1, historial.getEquipo().getId());
            ps.setString(2,historial.getObservacion());
            ps.setString(3, historial.getEstado());
            ps.setTimestamp(4, historial.getFeCreacion());
            ps.setString(5, historial.getUsrCreacion());
            
            ps.executeUpdate();

            ps.close();    
                      
                            
        } catch (SQLException e) {            
            e.printStackTrace();
//        } finally {
//            if (connection != null) {
//                try {
////                    connection.commit();
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
        }  
        
        
    }
    
    public void updateEquipo(InfoEquipo equipo,Connection connection)
    {
        
        String sql = "update info_equipo set status = ? where id_equipo = ?";       
      
        try{
                        
            PreparedStatement ps = connection.prepareStatement(sql); 
            
            ps.setString(1, equipo.getStatus());
            ps.setInt(2, equipo.getId());
            
            ps.executeUpdate();

            ps.close();    
                      
                            
        } catch (SQLException e) {            
            e.printStackTrace();
//        } finally {
//            if (connection != null) {
//                try {
////                    connection.commit();
////                    connection.close();
//                } catch (SQLException e) {                   
//                    e.printStackTrace();
//                }
//            }
//        }  
        }
        
    }
}
