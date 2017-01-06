/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreo.entidades;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author arsuarez
 */
public class Utils {     
    
    public Properties getConfiguration() 
    {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {

            inputStream = getClass().getClassLoader().getResourceAsStream("configuration.properties");
            // load a properties file
            if (inputStream != null) {
                properties.load(inputStream);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return properties;
    }
    
    
    public String getFullScript(String script,  String[] variables){
        StringBuffer scriptTmp = new StringBuffer();
        int contador = 0;

        for (int i = 0; i < script.length(); i++) {
            if (script.charAt(i) == '(') {
                contador++;
            }
        }
        if (variables.length == 1 && contador > variables.length) {
            StringBuffer varias = new StringBuffer();
            for (int i = 0; i < contador; i++) {
                String s = ","+variables[0];
                varias.append(s);
            }
            variables = varias.toString().split(",");
        }

        int cont = 0;
        for (int i = 0; i < script.length(); i++) {
            if (script.charAt(i) == '(') {
                for (int j = (i + 1); j < script.length(); j++) {
                    if (script.charAt(j) == ')') {
                        scriptTmp.append(variables[cont]);
                        cont++;
                        i = j + 1;
                        if ((j + 1) < script.length()) {
                            if (i < script.length()) {
                                //pregunta si el siguiente caracter es enter
                                if (script.charAt(j + 1) == '\n') {
                                    scriptTmp.append("\n");
                                }
                                //pregunta si el siguiente caracter es espacio
                                else if (script.charAt(j + 1) == ' ') {
                                    scriptTmp.append(" ");
                                }
                                //si no es ni espacio ni enter, debe añadir el caracter que se encuentre
                                else{
                                    scriptTmp.append(script.charAt(j + 1));
                                }
                            }
                        }
                        break;
                    }
                }
            } else {
                scriptTmp.append(script.charAt(i));
            }
        }

        return scriptTmp.toString();
    }
    
    /**
     * Funcion que sirve para reemplazar el script por un array list de variables
     * 
     */
    public String getFullScriptByArrayList(String script,  ArrayList<String> variables){
        StringBuffer scriptTmp = new StringBuffer();
        
        int cont = 0;
        for (int i = 0; i < script.length(); i++) {
            if (script.charAt(i) == '(') {
                for (int j = (i + 1); j < script.length(); j++) {
                    if (script.charAt(j) == ')') {
                        scriptTmp.append(variables.get(cont));
                        cont++;
                        i = j + 1;
                        if ((j + 1) < script.length()) {
                            if (i < script.length()) {
                                //pregunta si el siguiente caracter es enter
                                if (script.charAt(j + 1) == '\n') {
                                    scriptTmp.append("\n");
                                }
                                //pregunta si el siguiente caracter es espacio
                                else if (script.charAt(j + 1) == ' ') {
                                    scriptTmp.append(" ");
                                }
                                //si no es ni espacio ni enter, debe añadir el caracter que se encuentre
                                else{
                                    scriptTmp.append(script.charAt(j + 1));
                                }
                            }
                        }
                        break;
                    }
                }
            } else {
                scriptTmp.append(script.charAt(i));
            }
        }

        return scriptTmp.toString();
    }
    
        
    
}
