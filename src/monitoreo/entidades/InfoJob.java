/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.entidades;

import java.sql.Date;

/**
 *
 * @author arsuarez
 */
public class InfoJob {
    
    private int id;
    private InfoScript script;
    private String tipo;
    private String nombreJob;
    private String estado;
    private Date feEjecucion;
    private int intervalo;
    private String duracion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InfoScript getScript() {
        return script;
    }

    public void setScript(InfoScript script) {
        this.script = script;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreJob() {
        return nombreJob;
    }

    public void setNombreJob(String nombreJob) {
        this.nombreJob = nombreJob;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFeEjecucion() {
        return feEjecucion;
    }

    public void setFeEjecucion(Date feEjecucion) {
        this.feEjecucion = feEjecucion;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    
    
    
}
