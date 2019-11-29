/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import clientPart.Cliente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import serverPart.Server;
import trackerServer.ServerTracker;

/**
 *
 * @author berenice
 */
public class main {
    public static void main(String[] args) {
        ServerTracker tracker  = new ServerTracker();
        try {
            tracker.createTorrent("test.exe");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        tracker.start();
        Server servidor = new Server(new ArrayList<>());
        servidor.start();
        System.out.println("done");

        /*Cliente cliente = new Cliente();
        try{
            cliente.readAndConnect("test.torrent");
        }catch (IOException e){
            System.out.println("Valiendo trozo");
        }*/
    }
    
}
