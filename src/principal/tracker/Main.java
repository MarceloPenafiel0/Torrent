/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal.tracker;

import TrackerRMI.Tracker;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Jose
 */
public class Main {
    private static final int PUERTO = 1099;
    
    public static void main(String[] args) {
        Remote remote;
        
        try {
            remote = UnicastRemoteObject.exportObject(new Tracker(), 0);
            Registry registry = LocateRegistry.createRegistry(PUERTO);
            registry.bind("tracker", remote);
            System.out.println("Servidor escuchando en el puerto " + String.valueOf(PUERTO));
        } catch(AlreadyBoundException | RemoteException e) {}
    }
}
