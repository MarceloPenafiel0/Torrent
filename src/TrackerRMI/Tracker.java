package TrackerRMI;

import RemoteInterface.TrackerInt;
import trackerServer.HiloTracker;

import java.io.*;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tracker extends UnicastRemoteObject implements TrackerInt {

    private Map<String,FileInformation> archivosDisponibles ;
    private final int PUERTOT = 1099;//puerto del tracker
    private FileInformation fileInf;
    private int lastIP;
    public Tracker() throws RemoteException {
        recuperarObjeto();
        //System.out.println(archivosDisponibles.get("test.pdf").numPeersQueTienenArchivo);
    }

    public void recuperarObjeto(){
        try{
            ObjectInputStream archivo = null;
            File path = new File("archivosDisponibles");
            if (path.exists()){
                archivo = new ObjectInputStream(new FileInputStream("archivosDisponibles"));
                archivosDisponibles = (Map<String, FileInformation>) archivo.readObject();
                archivo.close();
            }else{
                archivosDisponibles = Collections.synchronizedMap(new HashMap());
            }
        }catch(IOException | ClassNotFoundException e){
            System.out.println("No se pudo abrir el archivo");
        }

    }
    @Override
    public synchronized String[] getAddress(String FileName) {
        fileInf = archivosDisponibles.get(FileName);
        int num = fileInf.getNumConjuntos();
        lastIP = num;
        String [] address = new String[num];
        for (int i =0;i<num;i++){
            address[i]=getIP(i,FileName);
        }
        return address;
    }

    @Override
    public String getAltAddress(int index,String FileName) {
        return getIP(0,FileName);
    }

    public synchronized String getIP(int actualPosition,String FileName){
        fileInf=archivosDisponibles.get(FileName);
        if (actualPosition+1>=fileInf.getListaIPs().size()){
            actualPosition = -1;
        }
        return fileInf.getListaIPs().get(actualPosition+1);
    }

    @Override
    public void updateAddress(String IP,String FileName){
        fileInf = archivosDisponibles.get(FileName);
        fileInf.modifyData(IP);
        this.grabarObjeto();
    }

    public synchronized void grabarObjeto(){
        try{
            ObjectOutputStream entrada = new ObjectOutputStream(new FileOutputStream("archivosDisponibles"));
            entrada.writeObject(archivosDisponibles);
            entrada.flush();
            entrada.close();
        }catch(IOException e){
            System.out.println("No se pudo abrir el archivo");
        }
    }

    public int getNumConjuntos() {
        return lastIP;
    }
    
    
}
