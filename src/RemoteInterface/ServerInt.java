package RemoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInt extends Remote {

    void transferGroup(ClientInt client,int k,int i, int numPeers,String fileName) throws RemoteException;

}

