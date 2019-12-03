package C_S_RMI;

import ClienteRMI.Cliente;
import RemoteInterface.ClientInt;
import RemoteInterface.ServerInt;
import RemoteInterface.TrackerInt;
import ServerRMI.Server;
import java.awt.Component;

import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class main {
    public static String IP="";
    public static void main(String[] args) {
       /*try {

            java.rmi.registry.LocateRegistry.createRegistry(1099);
            ServerInt server = new Server();
            InetAddress address = InetAddress.getLocalHost();
            Naming.rebind("rmi://"+IP+"/"+address.getHostAddress(),server);
            System.out.println("Servidor Listo");

            //
        }catch (RemoteException e){
            System.out.println("Error 00S");
        }catch (UnknownHostException e){
            System.out.println("Error 00IP");
        }catch (MalformedURLException e){
            System.out.println("Error 00NR");
        }*/
       //Para seleccionar .torrent
       forMain h = new forMain();
       h.selectFile();

    }

    
    //falta direccion .torrent
    public static void download (String direccion){
        try {
            Map<Integer, Object[]> map = Collections.synchronizedMap(new HashMap<>());
            //leer el .torrent
            BufferedReader to = new BufferedReader(new FileReader(new File(direccion)));
            //Obtencion del nombre del archivo
            String fileName = to.readLine();
            //Obtencion cadena hash para control de integridad
            String hash = to.readLine();
            to.close();
            Cliente client = new Cliente(fileName,map,hash);
            InetAddress address = InetAddress.getLocalHost();
            Naming.rebind("rmi://"+IP+"/"+address.getHostAddress(),client);
            System.out.println("Cliente remoto listo");
            client.startDownload(IP+"/tracker");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class forMain{
        
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
    
    public void selectFile(){
        JFrame parent = new JFrame("Elegir torrent");
        JFileChooser jf = new JFileChooser();
        jf.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filtroImagen=new FileNameExtensionFilter("TORRENT","torrent");
        jf.setFileFilter(filtroImagen);
        int result = jf.showOpenDialog(parent);
        //preguntar si da igual donde este file torrent
        if (result == JFileChooser.APPROVE_OPTION) {
            String name=jf.getSelectedFile().getName();
            copyFile(jf.getSelectedFile().getPath(), name);
            //download(name);
        }else{
            JOptionPane.showMessageDialog(null, "NO HA SELECCIONADO ARCHIVO", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        
    }
}