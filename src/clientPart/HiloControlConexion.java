/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientPart;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berenice
 */
public class HiloControlConexion extends Thread{
    private static Socket tracker;
    String ipFallida,fileName,IP,hash;
    Map<Integer,Object[]> map;
    int k,i,numPeers,Puerto;
    DataOutputStream dos;
    DataInputStream dis;

    public HiloControlConexion(String ipFallida, int k, int numPeers, int i ,  int Puerto, Map<Integer,Object[]> map, String hash, String fileName, DataOutputStream dos, DataInputStream dis){
        this.k=k;
        this.numPeers=numPeers;
        this.i=i;
        this.ipFallida=ipFallida;
        this.Puerto=Puerto;
        this.map=map;
        this.hash=hash;
        this.fileName=fileName;
        this.dos=dos;
        this.dis=dis;
    }

    @Override
    public void run() {
        reset();
    }


    private synchronized void reset(){
        try {
            dos.writeUTF("CONECTIONFAILED " + ipFallida);
            String [] res =dis.readUTF().split(" ");
            HiloDescarga hilo = new HiloDescarga(k,numPeers,i,res[0],Integer.parseInt(res[1]),map,hash,fileName,dos,dis);
            hilo.start();
        }catch (IOException e){
            System.err.println(e);
            e.printStackTrace();
        }/*finally{
            try {
                dos.close();
                dis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }*/
    }


}
