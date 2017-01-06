/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.entidades;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author arsuarez
 */
public class InfoEquipoHistorial {
    
    private int id;
    private InfoEquipo equipo;
    private String observacion;
    private String estado;
    private Timestamp feCreacion;
    private String usrCreacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InfoEquipo getEquipo() {
        return equipo;
    }

    public void setEquipo(InfoEquipo equipo) {
        this.equipo = equipo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFeCreacion() {
        return feCreacion;
    }

    public void setFeCreacion(Timestamp feCreacion) {
        this.feCreacion = feCreacion;
    }

    public String getUsrCreacion() {
        return usrCreacion;
    }

    public void setUsrCreacion(String usrCreacion) {
        this.usrCreacion = usrCreacion;
    }
    
    
}
