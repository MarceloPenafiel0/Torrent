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
<<<<<<< HEAD
    String ipServer;
    ServerInt servidor;
    TrackerInt tracker;
    String fileName;
    int k,numConjuntos,i;
    Cliente cliente;
    public HiloConexion(String ipServer, TrackerInt tracker,String fileName, int k,Cliente cliente,int numConjuntos, int i){
        this.ipServer = ipServer;
        this.tracker = tracker;
        this.fileName = fileName;
        this.k = k;
        this.cliente = cliente;
        this.numConjuntos = numConjuntos;
=======
    private String ipServer;
    private ServerInt servidor;
    private TrackerInt tracker;
    private String fileName;
    private int numConjuntos;
    public HiloConexion(String ipServer, TrackerInt tracker,String fileName,int numConjuntos){
        this.ipServer = ipServer;
        this.tracker = tracker;
        this.fileName = fileName;
        this.numConjuntos=numConjuntos;
>>>>>>> origin/master
    }
    
    @Override
    public void run() {
        conectarServer();
    }
    
    public synchronized void conectarServer(){
       // while(true){
            try {
                servidor = (ServerInt) Naming.lookup("rmi://"+ipServer);
<<<<<<< HEAD
                servidor.transferGroup(cliente, k, i,numConjuntos , fileName);
=======
                for (int k = 0;k<numConjuntos);
                //servidor.transferGroup(client, MIN_PRIORITY, MIN_PRIORITY, NORM_PRIORITY, fileName);
>>>>>>> origin/master
            } catch (Exception ex) {
                ipServer = tracker.getAltAddress(0, fileName);
                //ex.printStackTrace();
            } 
       // }
    }
}
