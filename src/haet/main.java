/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package haet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author danie
 */
public class main {
    public static void main(String[] args) {
        Process p;
        try {
          p = Runtime.getRuntime().exec("arp -a");
          p.waitFor();
          BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line = "";
          while ((line = reader.readLine())!= null) {
            System.out.println(line);
          }
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
    }
}
