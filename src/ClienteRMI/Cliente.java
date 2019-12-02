package ClienteRMI;

import RemoteInterface.ClientInt;
import RemoteInterface.TrackerInt;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import static clientPart.SHA1.SHA1;

public class Cliente extends UnicastRemoteObject implements ClientInt {

    private final int bytesize= 60000;
    private Map<Integer, Object[]> map;
    private String hash;
    private TrackerInt tracker;
    private String FileName;
    public Cliente(String FileName, Map<Integer, Object[]> map, String hash) throws RemoteException {
        this.map=map;
        this.hash=hash;
        this.FileName = FileName;
    }

    public void startDownload(String IPNAme){
        try {
            tracker = (TrackerInt) Naming.lookup("rmi://" + IPNAme);
            String []dir = tracker.getAddress(FileName);
            for (int i=0;i<dir.length;i++){
                HiloConexion hilo = new HiloConexion(dir[i],tracker,FileName,dir.length);
            }
        }catch (Exception e){
            
        }
    }

    @Override
    public synchronized boolean sendData(byte[] data, int len, int numPart) throws RemoteException {
        byte [] recievedData = new byte[bytesize];
        byte [] trash = new byte[bytesize];
        String calculatedHash="";
        String controlHash="";
        calculatedHash = SHA1(recievedData);
        controlHash = hash.substring(20*numPart,20*(numPart+1));
            if(calculatedHash.equals(controlHash)){
                Object [] array = {recievedData,len};
                map.put(numPart,array);
                return true;
            }
            else{
                return false;
            }
    }



}
