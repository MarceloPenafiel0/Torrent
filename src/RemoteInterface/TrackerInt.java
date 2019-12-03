package RemoteInterface;

import java.io.IOException;
import java.rmi.Remote;

public interface TrackerInt extends Remote {

    String[] getAddress(String FileName);

    String getAltAddress(int index,String FileName);

    void updateAddress(String IP,String FileName);

    public boolean createTorrent(String nombreArchivo,String primeraIP, int numPartes) throws IOException;

}
