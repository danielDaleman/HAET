/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.laboratorio2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author w guzman
 */
public class PruebaLectura {
	public static void main(String[] args) throws Exception {
            String path="C:\\Users\\w guzman\\Desktop\\prueba";
            final File carpeta = new File(path);
            vistorDeArchivos(carpeta);
	}
        //Poner la dieccion de la carpeta que se analizara
//        String path="C:\\Users\\w guzman\\Desktop\\prueba";
//        final File carpeta = new File(path);
        public static void vistorDeArchivos(final File carpeta) throws IOException{
            System.out.println("entre al vistor");
            for(final File ficheroCarpeta : carpeta.listFiles()){
                if(ficheroCarpeta.isDirectory()){
                    vistorDeArchivos(ficheroCarpeta);
                }else{
                    try (BufferedReader reader = new BufferedReader(new FileReader(ficheroCarpeta))){
                        String inputLine = null;
                        if((inputLine = reader.readLine()) != null){
                            if(inputLine.contains("$$")){
                                //salvar el path del documento , solo se esta obteniendo, no se guarda
                                System.out.println(ficheroCarpeta.getCanonicalPath());
                            }
                        }
                    
                    }catch (FileNotFoundException ex) {
                        Logger.getLogger(PruebaLectura.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }   
            }
        }

}
