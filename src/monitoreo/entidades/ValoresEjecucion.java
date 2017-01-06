/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.entidades;

import java.util.ArrayList;

/**
 *
 * @author arsuarez
 */
public class ValoresEjecucion {
    
    private String comando;
    private String ids;
    private String proceso;
    private String valores;
    private String job;
    private ArrayList<Integer> listEquipos;

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getIdsEquipo() {
        return ids;
    }

    public void setIdsEquipo(String ids) {
        String[] idArray = ids.split(":");
        listEquipos = new ArrayList<>();
        for(String id : idArray){
            listEquipos.add(Integer.parseInt(id));
        }
        this.ids = ids;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getValores() {
        return valores;
    }

    public void setValores(String valores) {
        this.valores = valores;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public ArrayList<Integer> getListEquipos() {
        return listEquipos;
    }

    public void setListEquipos(ArrayList<Integer> listEquipos) {
        this.listEquipos = listEquipos;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
    
    
    
}
