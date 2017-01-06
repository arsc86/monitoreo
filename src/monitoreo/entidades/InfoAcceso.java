/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.entidades;

/**
 *
 * @author arsuarez
 */
public class InfoAcceso {
    
    private int id;
    private String usuarioAcceso;
    private String passwordAcceso;
    private String idEquipo;
    private String ipEquipo;
    private String script;

    public String getUsuarioAcceso() {
        return usuarioAcceso;
    }

    public void setUsuarioAcceso(String usuarioAcceso) {
        this.usuarioAcceso = usuarioAcceso;
    }

    public String getPasswordAcceso() {
        return passwordAcceso;
    }

    public void setPasswordAcceso(String passwordAcceso) {
        this.passwordAcceso = passwordAcceso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getIpEquipo() {
        return ipEquipo;
    }

    public void setIpEquipo(String ipEquipo) {
        this.ipEquipo = ipEquipo;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
    
    
    
    
}
