/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackerServer;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author berenice
 */
public class ServerTracker extends Thread{
    private Map<String,FileInformation> archivosDisponibles ;
    private ServerSocket socketServer;
    private ArrayList<HiloTracker> listaHilosEnEjecucion = new ArrayList<>();
    private final int PUERTOT = 6000;//puerto del tracker
    
    public ServerTracker(){
        recuperarObjeto();
        //System.out.println(archivosDisponibles.get("test.pdf").numPeersQueTienenArchivo);
    }
    
    public void grabarObjeto() throws IOException{
        try{
            ObjectOutputStream entrada = new ObjectOutputStream(new FileOutputStream("archivosDisponibles"));
            entrada.writeObject(archivosDisponibles);
            entrada.flush();
            entrada.close();
        }catch(IOException e){
            System.out.println("No se pudo abrir el archivo");
        }
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
    
    
    public void run(){
        try {
            socketServer = new ServerSocket(PUERTOT);
            while (true){
                Socket client = socketServer.accept();
                InputStream IS=client.getInputStream();
                DataInputStream dis = new DataInputStream(IS);
                //DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                String nombreArchivo = dis.readUTF();
                System.out.println(nombreArchivo);
                HiloTracker hilo = new HiloTracker(nombreArchivo, client, archivosDisponibles.get(nombreArchivo),this);
                listaHilosEnEjecucion.add(hilo);
                hilo.start();
            }
        
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    public boolean archivoExiste(String nombre){
        return archivosDisponibles.containsKey(nombre);
    }
    
    
    public boolean createTorrent(String nombreArchivo) throws IOException{
        String data = "";
        try {
            data += InetAddress.getLocalHost().getHostAddress()+"\n";
            data += 6000+"\n";
            data += nombreArchivo+"\n";
            String ip = InetAddress.getLocalHost().getHostAddress();
            FileInformation fileInf = new FileInformation(nombreArchivo,ip);
            archivosDisponibles.put(nombreArchivo, fileInf);
            grabarObjeto();
            data += fileInf.getNumPartesArchivo()+"\n";
            data += codifyFile(nombreArchivo)+"\n";
            String []a = nombreArchivo.split("\\.");
            String ruta = a[0]+".torrent";
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(data);
            bw.close();
        } catch (UnknownHostException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    public String codifyFile(String nombre) throws IOException{
        String data = "";
        try {
            BufferedInputStream archivo = new BufferedInputStream(new FileInputStream(nombre));
            byte[] ar = new byte[60000];
            int in;
            while ((in=archivo.read(ar))!=-1)
                data += clientPart.SHA1.SHA1(ar);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return data;
    }
}
