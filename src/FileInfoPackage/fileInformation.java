package FileInfoPackage;

import java.io.Serializable;

/**
 * Created by Ahmed on 17/12/2015.
 */
public class fileInformation implements Serializable {

    String userName;
    public int portAsServer;

    public fileInformation(String userName){
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
