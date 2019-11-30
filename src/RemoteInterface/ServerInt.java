package RemoteInterface;

import java.rmi.RemoteException;

public interface ServerInt {

    void transferGroup(ClientInt client,int k,int i, int numPeers,String fileName) throws RemoteException;

}

