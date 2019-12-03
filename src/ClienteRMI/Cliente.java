package ClienteRMI;

import RemoteInterface.ClientInt;
import RemoteInterface.TrackerInt;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import static clientPart.SHA1.SHA1;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Cliente extends UnicastRemoteObject implements ClientInt {

    private final int bytesize= 60000;
    private Map<Integer, Object[]> map;
    private String hash;
    private TrackerInt tracker;
    private String FileName;
    /*public Cliente(String FileName, Map<Integer, Object[]> map, String hash) throws RemoteException {
        this.map=map;
        this.hash=hash;
        this.FileName = FileName;
    }*/
    public Cliente()throws RemoteException{
        map = new TreeMap<Integer, Object[]>();
    }

    public void startDownload(String IPNAme,String FileName,String hash,Map<Integer, Object[]> map){
        this.hash=hash;
        this.FileName = FileName;
        this.map = map;
        try {
            tracker = (TrackerInt) Naming.lookup("rmi://" + IPNAme);
            String []dir = tracker.getAddress(FileName);
            for (int k=0;k<dir.length;k++){
                HiloConexion hilo = new HiloConexion(dir[k],tracker,FileName,k,this,dir.length,0);
                hilo.start();
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

    public boolean copyFile(String fromFile, String toFile) {
        File origin = new File(fromFile);
        File destination = new File(toFile);
        if (origin.exists() && !destination.exists()) {
            try {
                InputStream in = new FileInputStream(origin);
                OutputStream out = new FileOutputStream(destination);
                // We use a buffer for the copy (Usamos un buffer para la copia).
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    
    public String selectFile(){
        JFrame parent = new JFrame("Elegir torrent");
        JFileChooser jf = new JFileChooser();
        jf.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filtroImagen=new FileNameExtensionFilter("TORRENT","torrent");
        jf.setFileFilter(filtroImagen);
        int result = jf.showOpenDialog(parent);
        //preguntar si da igual donde este file torrent
        if (result == JFileChooser.APPROVE_OPTION) {
            //String name=jf.getSelectedFile().getName();
            //copyFile(jf.getSelectedFile().getPath(), name);
            return jf.getSelectedFile().getPath();
            //download(path);
        }else{
            JOptionPane.showMessageDialog(null, "NO HA SELECCIONADO ARCHIVO", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return "";
    }
    
    public void createTorrent(){
        JFrame parent = new JFrame("Elegir torrent");
        JFileChooser jf = new JFileChooser();
        jf.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = jf.showOpenDialog(parent);
        //preguntar si da igual donde este file torrent
        if (result == JFileChooser.APPROVE_OPTION) {
            String name=jf.getSelectedFile().getName();
            String path = jf.getSelectedFile().getPath();
            //copyFile(path, name);
            copyFile(path, "./.clonedFiles/"+name);
            try {
                //necesito ver como me conecto al torrent, obtener direccion ip del torrent sin tener un .torrent de donde leer.
                tracker.createTorrent(name);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "NO SE HA CREADO TORRENT. HA OCURRIDO UN ERROR", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "NO HA SELECCIONADO ARCHIVO", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
    }

}
