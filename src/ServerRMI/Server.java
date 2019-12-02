package ServerRMI;

import RemoteInterface.ClientInt;
import RemoteInterface.ServerInt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInt {

    public Server () throws RemoteException {}

    @Override
    public void transferGroup(ClientInt client, int k, int i, int numPeers, String fileName) throws RemoteException {

    }
}
