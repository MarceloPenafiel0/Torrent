/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientPart;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

/**
 *
 * @author berenice
 */
public class HiloControlArchivo extends Thread {
    Map<Integer,Object[]> map;
    int numPartes;
    String fileName;
    DataInputStream dis;
    DataOutputStream dos;
    BufferedOutputStream bosS;
    BufferedOutputStream bosC;
    public HiloControlArchivo (Map<Integer,Object[]> map,String fileName,int numPartes,DataInputStream dis, DataOutputStream dos){
        this.map=map;
        this.fileName=fileName;
        this.numPartes=numPartes;
        this.dis=dis;
        this.dos=dos;
    }


    @Override
    public void run() {
        escribir();
    }

    private synchronized void escribir(){
        try {
            final File newFile = new File( ".\\.clonedFiles\\"+fileName);
            bosC = new BufferedOutputStream(new FileOutputStream(fileName,true));
            bosS = new BufferedOutputStream(new FileOutputStream(newFile,true));
            int i = 0;
            Object[] array;
            while (i != numPartes+1) {
                if ((array = map.get(i)) != null) {
                    bosC.write((byte[])array[0],0,(int) array[1]);
                    bosS.write((byte[])array[0],0,(int) array[1]);
                    i++;
                } else {
                    continue;
                }
            }
            InetAddress address = InetAddress.getLocalHost();
            dos.writeUTF("COMPLETED "+address.getHostAddress());
            System.out.println("Ya esta");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bosC.close();
                bosS.close();
                dos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
