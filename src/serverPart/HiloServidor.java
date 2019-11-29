/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverPart;

import javax.swing.plaf.TableHeaderUI;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author berenice
 */
public class HiloServidor extends Thread{
    final int bytesize=60000;//256KB
    Socket cliente;
    String nombArchivo;
    int k,i,in, numGrup;
    byte[] sendBytes;
    BufferedInputStream bis;
    BufferedOutputStream bos;

    public HiloServidor (Socket cliente,int k, int i, int numGrup,String nombArchivo){
        this.cliente=cliente;
        this.k=k;
        this.i=i;
        this.nombArchivo=nombArchivo;
        this.numGrup=numGrup;
    }


    @Override
    public void run() {
        enviar();
    }

    private synchronized void enviar(){
        final File localFile = new File( ".\\.clonedFiles\\"+nombArchivo);
        sendBytes = new byte[bytesize];
        String resp ="";
        try {
            //para sacar bytes del archivo
            bis = new BufferedInputStream(new FileInputStream(localFile));
            bos = new BufferedOutputStream(cliente.getOutputStream());
            //flujo de comunicacion
            DataOutputStream dos=new DataOutputStream(cliente.getOutputStream());
            DataInputStream dis = new DataInputStream(cliente.getInputStream());
            //skip inicial: como el archivo manda un num especifico, se salta esos iniciales
            bis.skip(k*bytesize);
            // bis.skip((k)*(bytesize/numGrup));



            //Va a la parte especificada por i para reanudar la transferecia
            if(i!=0)
                for(int j=i;j>0;j--)
                    bis.skip((numGrup -1)*bytesize);

            in = bis.read(sendBytes);

            //Transfiere partes del archivo
            while (in != -1){
                System.out.println("in value hiloServidor: "+in);
                bos.write(sendBytes,0,in);
                bos.flush();
                System.out.println(k+ numGrup *i);//debo enviar?? -> Nel, lo calcula el cliente
                //dos.writeUTF(String.valueOf(1));
                resp=dis.readUTF();
                System.out.println("res:"+resp);
                if(resp.equals("CORRECT")){
                    bis.skip((numGrup -1)*bytesize);
                    in=bis.read(sendBytes);
                    //cuando se cambia el value de i???
                    i++;
                }else if (resp.equals("DAMAGED")){
                    bos.flush();
                    Thread.sleep(20);
                    ///bos.flush();
                    continue;
                }else{
                    dos.writeUTF("COMMAND NOT FOUND");
                }
            }
            bos.close();
            dis.close();
            dos.close();
            bis.close();
            cliente.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
