package RemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInt extends Remote {

    boolean sendData(byte[] data, int len, int numPart) throws RemoteException;

}
