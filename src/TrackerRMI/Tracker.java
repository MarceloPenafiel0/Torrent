package TrackerRMI;

import RemoteInterface.TrackerInt;
import trackerServer.HiloTracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tracker extends UnicastRemoteObject implements TrackerInt {

    private Map<String,FileInformation> archivosDisponibles ;
    private ServerSocket socketServer;
    private ArrayList<HiloTracker> listaHilosEnEjecucion = new ArrayList<>();
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
    public String[] getAddress(String FileName, String IP) {
        fileInf = archivosDisponibles.get(FileName);

        int num = fileInf.getNumConjuntos();
        lastIP = num;
        String [] address = new String[num];
        for (int i =0;i<num;i++){
            address[i]=getIP(i);
        }
        return address;
    }

    @Override
    public String getAltAddress(int index) {
        return getIP(0);
    }

    public String getIP(int actualPosition){
        if (actualPosition+1>=fileInf.getListaIPs().size()){
            actualPosition = -1;
        }
        return fileInf.getListaIPs().get(actualPosition+1);
    }
}