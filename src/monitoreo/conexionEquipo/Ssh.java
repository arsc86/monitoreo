
package monitoreo.conexionEquipo;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;
import java.net.InetAddress;
import org.apache.commons.lang3.SystemUtils;

/**
 * Clase para crear conexion ssh 
 */
public class Ssh extends JSch {    
    private String hostname;
    private String prompt;
    private String tipo;    
    static final String routerPrompt = "#";
    static final String switchPrompt = ">";

    private boolean continuar = false;

    private Session session;

    private static final int TIMEOUT_CONNECT = 300000;
    private static final int MAX_TIMER_RECEIVE_DATA = TIMEOUT_CONNECT;

    private OutputStreamWriter datosHaciaServidor;
    private InputStream datosDesdeServidor;
    Channel canal;

    private int contNoData = 0;

    private boolean imprimirBuffer = false;
    private boolean probarPing = false;
    static final int MAX_NO_DATA = 30;
    
    public Ssh(){}
    
    public Ssh(String host, String tipo , boolean imprimirBuffer, boolean probarPing) {
        super();
        hostname            = host;
        session             = null;
        prompt              = "";
        datosHaciaServidor  = null;
        datosDesdeServidor  = null; 
        this.imprimirBuffer = imprimirBuffer;
        this.probarPing     = probarPing;    
        switch(tipo)
        {
            case "ROUTER":
                prompt = Ssh.routerPrompt;                                
                break;
                
            case "SWITCH":
                prompt = Ssh.switchPrompt;
                break;
        }
    }

    /**
    * Funcion que sirve para inicializar la conexion al elemento
    * 
    * @param usuario String
    * @param clave   String
    */
    public void inicializarConexion(String usuario, String clave) throws JSchException, IOException {
        
        //Configuracion 
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session = this.getSession(usuario, hostname, 22);

        session.setPassword(clave);
        session.setConfig(config);
        session.connect(Ssh.TIMEOUT_CONNECT);

        //Canal
        canal = session.openChannel("shell");
        
        //Definir buffers antes de conectarse caso contrario puede provocar un timeout y cortar la comunicacion
        datosHaciaServidor = new OutputStreamWriter(canal.getOutputStream());
        datosDesdeServidor = canal.getInputStream();

        canal.connect(Ssh.TIMEOUT_CONNECT);
        ((ChannelShell) canal).setPtyType("vt102");
    }

    /**
    * Funcion que sirve para verificar el ping hacia el elemento
    * 
    * @param host String
    */
    public static boolean hostEsAlcanzable(String host) throws Exception {
        List<String> command = new ArrayList<>();
        command.add("ping");
        if (SystemUtils.IS_OS_WINDOWS)
        {
            command.add("-n");
        }
        else if (SystemUtils.IS_OS_UNIX)
        {
            command.add("-c");
        }
        else
        {
            throw new UnsupportedOperationException("Unsupported operating system");
        }

        command.add("5");
        command.add(host);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        BufferedReader standardOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String outputLine;

        while ((outputLine = standardOutput.readLine()) != null)
        {
            
            // Picks up Windows and Unix unreachable hosts
            if (outputLine.toLowerCase().contains("destination host unreachable")||
                outputLine.toLowerCase().contains("request timeout"))
            {
                return false;
            }
        }
        
        return true;
       
    }
    
    /**
    * Funcion que sirve para conectar y autenticar hacia el elemento
    * 
    * @author Francisco Adum <fadum@telconet.ec>
    * @version 1.0 7-01-2015
    * 
    * @param usuario String
    * @param clave   String
    */
    public void conectar(String usuario, String clave) throws Exception{
        if (probarPing) {
            if(!hostEsAlcanzable(hostname)){
                throw new IOException("[ " + hostname + " ] El host no es alcanzable a nivel de red ...");
            }
        }

        inicializarConexion(usuario, clave);
        leerHasta(prompt, true);
        escribir("enable");
        leerTiempo();
    }

    /**
    * Funcion que sirve para verificar que la sesion hacia el elemento
    * este activa
    * 
    * @author Francisco Adum <fadum@telconet.ec>
    * @version 1.0 7-01-2015
    * 
    * @return boolean
    */
    public boolean estaConectado() {
        return session != null && session.isConnected();
    }

    /**
    * Funcion que sirve para desconectarse del elemento
    * 
    * @author Francisco Adum <fadum@telconet.ec>
    * @version 1.0 7-01-2015
    */
    public void desconectar() {
        session.disconnect();
    }

    /**
    * Funcion que sirve para escribir en el olt, se concatena con un enter
    * 
    * @author Francisco Adum <fadum@telconet.ec>
    * @version 1.0 7-01-2015
    * 
    * @param valor String
    */
    public void escribir(String valor) throws Exception {
        datosHaciaServidor.flush();
        datosHaciaServidor.write(valor + "\n");
        datosHaciaServidor.flush();
        // Visualizar
        if (imprimirBuffer) {
            System.out.println(valor);
        }
    }
    
    /**
    * Funcion que sirve para escribir en el olt, se usa StringBuilder
    * 
    * @author Francisco Adum <fadum@telconet.ec>
    * @version 1.0 7-01-2015
    * 
    * @param valor String
    */
    public void write(String valor) throws IOException {
        datosHaciaServidor.write((new StringBuilder()).append(valor).append("\n").toString());
        datosHaciaServidor.flush();

    }
    
    /**
     * Funcion que sirve para ejecutar un script completo linea por linea con un delay de 2 segundos
     *
     *
     * @param script String
     * @param opcion String
     * @return String
     * @throws InterruptedException
     */
    public String ejecutarScript(String script, String opcion) throws Exception {
        String resultado = "";
        String[] scriptTmpSplit = script.split("\n");
        for (int h = 0; h < scriptTmpSplit.length; h++) {
            write(scriptTmpSplit[h].trim());
            Thread.sleep(2000);
            if (opcion.equalsIgnoreCase("leerHasta")) {
                resultado = leerHasta("#", true);
            } else if (opcion.equalsIgnoreCase("leerTiempo")) {
                resultado = leerTiempo();
            }
        }

        return resultado;
    }
    
    /**
     * Funcion que sirve para verificar el caracter que devuelve el elemento
     *
     * @author Francisco Adum <fadum@telconet.ec>
     * @version 1.0 5-12-2014
     *
     * @param cadena String
     * @return boolean
     */
    public boolean verificarCaracterAnterior (String cadena){
        boolean flag = false;
        if(cadena.endsWith("0#") || cadena.endsWith("1#") || cadena.endsWith("2#") || cadena.endsWith("3#") || cadena.endsWith("4#") || 
           cadena.endsWith("5#") || cadena.endsWith("6#") || cadena.endsWith("7#") || cadena.endsWith("8#") || cadena.endsWith("9#")){
            flag = true;
        }           
        
        return flag;
    }
    
    /**
     * Funcion que sirve para leer lo que devuelve el olt, en espera por 2 segundos
     *
     * @author Francisco Adum <fadum@telconet.ec>
     * @version 1.0 5-12-2014
     *
     * @return String
     */
    public String leerTiempo() throws Exception {

        List<String> results = new ArrayList<String>();
        boolean exit = false;
        byte[] tmp = new byte[1024];
        int readingTime = 0;
        while (readingTime <= 2000) {
            while (datosDesdeServidor.available() > 0) {
                int i = datosDesdeServidor.read(tmp, 0, 1024);
                if (i < 0) {
                    exit = true;
                    break;
                }
                String buffer = new String(tmp, 0, i);
                results.add(buffer);
            }
            if (exit) {
                break;
            } else {
                readingTime += 500;
                Thread.sleep(500);
            }
        }

        return results.toString();
    }

    /**
     * Funcion que sirve para leer lo que devuelve el olt hasta que encuentre un prompt
     *
     * @author Francisco Adum <fadum@telconet.ec>
     * @version 1.0 5-12-2014
     *
     * @param patron                    String
     * @param detenerSiContienePrompt   boolean
     * @return boolean
     */
    public String leerHasta(String patron, boolean detenerSiContienePrompt) throws Exception {
			// Temporizador para frenar seguir recibiendo datos sin parar en
        // caso de no encontrar
        // el patron
        TimerTask temporizador = new TimerTask() {
            public void run() {
                continuar = false;
            }
        };

			// Hacer el timer una daemon thread. Una vez terminada su funcion
        // muere
        Timer t = new Timer(true);
			// Si no se encuentra el patron leer hasta el tiempo permitido por
        // la constante MAX_TIMER_RECEIVE_DATA
        t.schedule(temporizador, MAX_TIMER_RECEIVE_DATA);

        // Contador caracteres seguidos
        contNoData = 0;
        // Caracter anterior
        char caracterAnterior;
        // Ultimo caracter del patron
        char ultimoCaracter = patron.charAt(patron.length() - 1);

        String ultimoCaractPrompt = prompt;

        // Buffer
        StringBuffer sb = new StringBuffer();

        // Leer caracter
        char ch = (char) datosDesdeServidor.read();
        // Setear el valor continuar para leer datos del stream
        continuar = true;

        do {
            // Debug Pantalla
            if (imprimirBuffer) {
                System.out.print(ch);
            }
            // Obtener el caracter anterior
            caracterAnterior = ch;
            // Agregar caracter al buffer
            sb.append(ch);

				// Validar que el caracter actual sea un - y que la longitud de todo 
            // el buffer sea al menos  8 caracteres (la longitud de la cadena --More--
            // Caso contrario se produciria una excepcion de Indice de array fuera
            // de los limites al ejecutar el subtring 
            if (ch == '-' && sb.toString().length() >= 8) {
                // Eliminar el --More--
                if (sb.toString().substring(sb.toString().length() - 8,
                    sb.toString().length()).equals("---- More ( Press 'Q' to break ) ----")) {
						// Enviar caracter para desplazar la pantalla hacia
                    // abajo y seguir leyendo
                    datosHaciaServidor.write(" \n");
                    datosHaciaServidor.flush();
                }
            }

            // Alcanzamos el ultimo caracter?
            if (ch == ultimoCaracter) {
                if (sb.toString().endsWith(patron) && !verificarCaracterAnterior(sb.toString())) {
                    t.cancel();
                    String tmpStr = sb.toString();
						// Eliminar la cadena --More-- y los backspaces que
                    // aparecen como caracteres basura

                    tmpStr = tmpStr.replaceAll("\b", "");
                    tmpStr = tmpStr.replaceAll("---- More ( Press 'Q' to break ) ----", "");
                    return tmpStr;
                }
            }

            if (detenerSiContienePrompt == true) {
                if (sb.toString().endsWith(ultimoCaractPrompt) && !verificarCaracterAnterior(sb.toString())) {
                    t.cancel();
                    String tmpStr = sb.toString();
                        // Eliminar la cadena --More-- y los backspaces que
                    // aparecen como caracteres basura	
                    tmpStr = tmpStr.replaceAll("\b", "");
                    tmpStr = tmpStr.replaceAll("---- More ( Press 'Q' to break ) ----", "");
                    return tmpStr;
                }
            }

            // Leer caracter
            ch = (char) datosDesdeServidor.read();
                // El caracter devuelto no es ascii? El caracter actual es igual
            // al anterior?
            if ((int) ch > 255 && ch == caracterAnterior) {
                contNoData++;
            }

					// Si se supera el numero de caracteres no seguidos ASCII
            // recibidos, se detiene
            if (contNoData >= MAX_NO_DATA) {
                t.cancel();
                continuar = false;
            }

        } while (continuar && ch > 0);

        return null;
    }

    public boolean sePermitePruebaPing() {
        return probarPing;
    }

    public void habilitarPruebaPing(boolean probarPingB) {
        probarPing = probarPingB;
    }

    public boolean sePermiteImprimirBuffer() {
        return imprimirBuffer;
    }

    public void permitirImprimirBuffer(boolean imprimirBufferB) {
        imprimirBuffer = imprimirBufferB;
    }

    public String obtenerPrompt() {
        return prompt;
    }
}