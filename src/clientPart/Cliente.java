/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientPart;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author berenice
 */
public class Cliente {
    ArrayList<HiloControlArchivo> listConAr;
    ArrayList<HiloControlConexion> listConCo;
    ArrayList<HiloDescarga> listDescargas;
    Socket tracker;
    public Cliente(){
        this.listConAr = new ArrayList<>();
        this.listConCo= new ArrayList<>();
        this.listDescargas = new ArrayList<>();
    }

    public void connectServer(int k, int numPeers,int i, String IP, int Puerto,Map<Integer,Object[]> map,String hash,String fileName,DataOutputStream dos,DataInputStream dis){
        
        System.out.println("verifying i on Cliente when he tries to connect to a server : "+i);
        //solo crea hilo donde realizare la ocnexion ...
        HiloDescarga hilo = new HiloDescarga(k,numPeers,i,IP,Puerto,map,hash,fileName,dos,dis);
        this.listDescargas.add(hilo);
        hilo.start();
    }

    public void readAndConnect(String direccion) throws IOException{

            Map<Integer,Object[]> map = Collections.synchronizedMap(new HashMap<>());
            //leer el .torrent
            BufferedReader to = new BufferedReader(new FileReader(new File(direccion)));
            String ip = to.readLine();
            int puerto = Integer.parseInt(to.readLine());
            String fileName= to.readLine();
            //int size = Integer.parseInt(to.readLine());
            int numPartes= Integer.parseInt(to.readLine());
            String cadenaHash = to.readLine();
            to.close();
            //fin lectura.torrent
            int in;
            //conecta con el tracker
            tracker = new Socket(ip,puerto);
            DataInputStream dis=new DataInputStream(tracker.getInputStream());
            DataOutputStream dos = new DataOutputStream(tracker.getOutputStream());
            dos.writeUTF(fileName);
            
            String[] entrada= dis.readUTF().split(" ");

           // int i = 0;//borrar
            /*HiloControlArchivo hiloCA = new HiloControlArchivo(map,fileName,numPartes,dis,dos);
            this.listConAr.add(hiloCA);   //De momento no es necesario.
            hiloCA.start();*/

            while (!entrada[0].equals("FINISHED")){
                try {
                    connectServer(Integer.parseInt(entrada[1]), Integer.parseInt(entrada[2]), 0,entrada[3], Integer.parseInt(entrada[4]), map, cadenaHash, fileName,dos,dis);
                    //creo que hay un problema con i... no hay por que se reinica el i con cada conjunto enviado :)
                    dos.writeUTF("DONE");
                } catch (NumberFormatException e) {
                    System.err.println(e);
                    //dos.writeUTF("WRONG");
                    break;
                } catch (IOException e) {
                    dos.writeUTF("WRONG");
                }finally {
                    entrada=dis.readUTF().split(" ");
                }
            }

            HiloControlArchivo hiloCA = new HiloControlArchivo(map,fileName,numPartes,dis,dos);
            this.listConAr.add(hiloCA);   //De momento no es necesario.
            hiloCA.start();


    }


}
