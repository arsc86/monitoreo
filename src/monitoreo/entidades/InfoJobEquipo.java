/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.entidades;

/**
 *
 * @author arsuarez
 */
public class InfoJobEquipo {
    
    private int id;
    private InfoJob job;
    private InfoEquipo equipo;
    private String estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InfoJob getJob() {
        return job;
    }

    public void setJob(InfoJob job) {
        this.job = job;
    }

    public InfoEquipo getEquipo() {
        return equipo;
    }

    public void setEquipo(InfoEquipo equipo) {
        this.equipo = equipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
    
    
}
