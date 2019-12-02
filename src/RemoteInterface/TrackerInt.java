package RemoteInterface;

import java.rmi.Remote;

public interface TrackerInt extends Remote {

    String[] getAddress(String FileName);

    String getAltAddress(int index,String FileName);

    void updateAddress(String IP,String FileName);

}
