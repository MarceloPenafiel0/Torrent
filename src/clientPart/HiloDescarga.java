/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientPart;

import static clientPart.SHA1.SHA1;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author berenice
 */
public class HiloDescarga extends Thread {
    int k, numPeers,Puerto;
    String IP,hash,fileName;
    Socket server;
    BufferedInputStream bis;

    DataInputStream dis,disT;
    DataOutputStream dos,dosT;
    Map<Integer,Object[]> map;
    int i;
    final int bytesize =60000;
    public HiloDescarga(int k, int numPeers, int i ,String IP, int Puerto, Map<Integer,Object[]> map,String hash,String fileName,DataOutputStream dosT,DataInputStream disT){
        this.k=k;
        this.numPeers=numPeers;
        this.i=i;
        this.IP=IP;
        this.Puerto=Puerto;
        this.map=map;
        this.hash=hash;
        this.fileName=fileName;
        //flujo entrada salida para poder conectarse con el tracker y mandar mensajes de conectionfailed
        this.dosT=dosT;
        this.disT=disT;
        try{
            //inicio la conexion con el servidor que tiene el archivo
            System.out.println("inicio la conexion con el servidor que tiene el archivo");
            server = new Socket(IP,Puerto);
            dos = new DataOutputStream(server.getOutputStream());
            dos.writeUTF(k+" "+i+" "+numPeers+" "+fileName);//ojo

        }catch (IOException e){e.printStackTrace();}
    }

    @Override
    public void run(){
        recibir();
    }

    private synchronized void recibir(){
        try {
            byte [] recievedData = new byte[bytesize];
            byte [] trash = new byte[bytesize];
            bis = new BufferedInputStream(server.getInputStream());
            dis = new DataInputStream( server.getInputStream());
            int in;
            int num;
            String calculatedHash="";
            String controlHash="";
            while((in=bis.read(recievedData))!=-1){
                //num = Integer.parseInt(dis.readUTF());
                System.out.println("*"+in);
                System.out.println("+"+i);
                num=k+numPeers*i;
                calculatedHash = SHA1(recievedData);
                controlHash = hash.substring(20*num,20*(num+1));
                if(calculatedHash.equals(controlHash)) {
                    System.out.println("manda correcto");
                    dos.writeUTF("CORRECT");
                    Object [] array = {recievedData,in};
                    map.put(num,array);
                    System.out.println("value i hilo descarga: "+i);
                    i++;
                }
                else{
                    dos.writeUTF("DAMAGED");
                    bis.read(trash);
                    Thread.sleep(10);
                }
                //Thread.sleep(1000);
            }
            System.out.println("Sali del while");
        }catch (IOException e){
            e.printStackTrace();
            HiloControlConexion hilo = new HiloControlConexion(IP,k,numPeers,i,Puerto,map,hash,fileName,dosT,disT);
            hilo.start();
        }catch(InterruptedException e){
           e.printStackTrace();
        }finally {
            try {
                bis.close();
                dis.close();
                dos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
