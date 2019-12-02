package C_S_RMI;

import ClienteRMI.Cliente;
import RemoteInterface.ClientInt;
import RemoteInterface.ServerInt;
import RemoteInterface.TrackerInt;
import ServerRMI.Server;

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

public class main {
    public static String IP="";
    public static void main(String[] args) {
        try {

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
        }

    }

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
