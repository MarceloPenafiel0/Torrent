/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClienteRMI;

/**
 *
 * @author berenice
 */
public class HiloConexion  extends Thread{
    String ipServer;
    
    public HiloConexion(String ipServer){
        this.ipServer = ipServer;
    }
    
    @Override
    public void run() {
        
    }
}
