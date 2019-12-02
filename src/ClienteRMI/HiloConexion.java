/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClienteRMI;

import ServerRMI.Server;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berenice
 */
public class HiloConexion  extends Thread{
    String ipServer;
    Server servidor;
    public HiloConexion(String ipServer){
        this.ipServer = ipServer;
    }
    
    @Override
    public void run() {
        
    }
    
    public void conectarServer(){
        while(true){
            try {
                servidor = (Server) Naming.lookup("rmi://"+ipServer);
                
            } catch (Exception ex) {
                Logger.getLogger(HiloConexion.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
}
