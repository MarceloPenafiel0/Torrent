/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClienteRMI;

import RemoteInterface.ServerInt;
import RemoteInterface.TrackerInt;
import ServerRMI.Server;
import TrackerRMI.Tracker;
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
    ServerInt servidor;
    TrackerInt tracker;
    String fileName;
    public HiloConexion(String ipServer, TrackerInt tracker,String fileName){
        this.ipServer = ipServer;
        this.tracker = tracker;
        this.fileName = fileName;
    }
    
    @Override
    public void run() {
        conectarServer();
    }
    
    public void conectarServer(){
        while(true){
            try {
                servidor = (ServerInt) Naming.lookup("rmi://"+ipServer);
                int numConjuntos = tracker.
                for (int k = 0;k<)
                //servidor.transferGroup(client, MIN_PRIORITY, MIN_PRIORITY, NORM_PRIORITY, fileName);
            } catch (Exception ex) {
                ipServer = tracker.getAltAddress(0, fileName);
                //ex.printStackTrace();
            } 
        }
    }
}
