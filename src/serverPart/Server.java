/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverPart;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author berenice
 */
public class Server extends Thread {
    ArrayList <HiloServidor> lista;
    ServerSocket serverSocket;


    public Server(ArrayList<HiloServidor> lista) {
        this.lista = lista;
    }

    public void run(){
        try {
            serverSocket = new ServerSocket(5000);

            while (true) {
                Socket client = serverSocket.accept();
                DataInputStream dis = new DataInputStream(client.getInputStream());
                String[] info = dis.readUTF().split(" ");
                HiloServidor hilo = new HiloServidor(client, Integer.parseInt(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]), info[3]);
                lista.add(hilo);
                hilo.start();
            }

        }catch (Exception e){
            System.err.println(e);
            e.printStackTrace();
        }
    }

}
