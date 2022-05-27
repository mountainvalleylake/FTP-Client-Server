package FileInfoPackage;

import java.io.Serializable;

public class clientInfo implements Serializable{
    public String userName;
    public String address;
    public int portAsServer;
    public clientInfo(String userName, String address, int portAsServer){
        this.userName = userName;
        this.address = address;
        this.portAsServer = portAsServer;
    }

}
