package RemoteInterface;

import java.rmi.Remote;

public interface TrackerInt extends Remote {

    String[] getAddress(String FileName, String IP);

    String getAltAddress(int index);

}
