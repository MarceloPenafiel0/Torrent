/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackerServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author berenice
 */
public class HiloTracker extends Thread{
    Socket cliente;
    String nombreArchivo;
    FileInformation fileInf;
    ServerTracker s;
    //DataInputStream dis ;
    
    public HiloTracker(String nombreArchivo,Socket cliente,FileInformation fileInf,ServerTracker s){
        this.cliente = cliente;
        this.nombreArchivo = nombreArchivo;
        this.fileInf = fileInf;
        this.s =s;
        //this.dis=dis;
        
    }
    
    @Override
    public void run(){
        try{
            DataInputStream dis = new DataInputStream(cliente.getInputStream());
            DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
            int actualPosition = 0;
            String res = "";
            for (int k = 0;k<fileInf.numConjuntos;k++){
                String mensaje = "CONNECT "+k+" "+fileInf.numConjuntos+" "+getIP(actualPosition++)+" "+5000; 
                dos.writeUTF(mensaje);
                //res = dis.readUTF((DataInput) cliente.getInputStream());
                res = dis.readUTF();
                if (res.equals("WRONG")){
                    k--;
                }
            }
            dos.writeUTF("FINISHED");
            actualPosition = 0;
            while (true){
                //res = dis.readUTF((DataInput) cliente.getInputStream());
                res = dis.readUTF();
                if (res.split(" ")[0].equals("CONECTIONFAILED")){
                    dos.writeUTF(dealFail(actualPosition, res));
                }
                if (res.split(" ")[0].equals("COMPLETED")){
                    fileInf.modifyData(res.split(" ")[1]);
                    s.grabarObjeto();
                    break;
                }
            }
            dis.close();
            dos.close();
            cliente.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public String dealFail(int actualPosition,String mensaje){
        return "CONNECT "+mensaje.split(" ")[1]+" "+fileInf.numConjuntos+" "+getIP(actualPosition++)+" "+5000;
    }
    
    public String getIP(int actualPosition){
        if (actualPosition+1>=fileInf.getListaIPs().size()){
            actualPosition = -1;
        }
        return fileInf.getListaIPs().get(actualPosition+1);
    }
        
}
