package ServerRMI;

import RemoteInterface.ClientInt;
import RemoteInterface.ServerInt;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInt {
    BufferedInputStream archivo_enviar;
    final int bytesize=60000;
    public Server () throws RemoteException {}
    byte[] sendBytes;
    int in;
    boolean situacion_archivo;

    @Override
    public void transferGroup(ClientInt client, int k, int i, int numPeers, String fileName) throws RemoteException {
        final File localFile = new File( ".\\.clonedFiles\\"+fileName);
        sendBytes = new byte[bytesize];
        try {
            archivo_enviar = new BufferedInputStream(new FileInputStream(localFile));
            //skip inicial: como el archivo manda un num especifico, se salta esos iniciales
            archivo_enviar.skip(k*bytesize);
            //Va a la parte especificada por i para reanudar la transferecia
            if(i!=0)
                for(int j=i;j>0;j--)
                    archivo_enviar.skip((numPeers -1)*bytesize);
            in = archivo_enviar.read(sendBytes);
            int num;
            while (in != -1){
                num=k+numPeers*i;

                situacion_archivo = client.sendData(sendBytes, in,num);

                situacion_archivo = client.sendData(sendBytes,in, num);

                if (situacion_archivo){
                    archivo_enviar.skip((numPeers -1)*bytesize);
                    in=archivo_enviar.read(sendBytes);
                    i++;
                }else{
                    continue;
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
