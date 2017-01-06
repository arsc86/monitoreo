/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.entidades;

/**
 *
 * @author arsuarez
 */
public class InfoScript {
    
    private int id;
    private String nombreScript;
    private String script;
    private String estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreScript() {
        return nombreScript;
    }

    public void setNombreScript(String nombreScript) {
        this.nombreScript = nombreScript;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
    
}
