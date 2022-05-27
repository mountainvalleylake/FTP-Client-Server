package Server;

import FileInfoPackage.fileInformation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;

public class ClientConnection {
    private Main main;
    ServerConnect serverConnect;
    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    public TextField nameField;

    @FXML
    public TextField ipAddress;

    @FXML
    private Button connectButton;

    @FXML
    void connectToServer(ActionEvent event) {
        main.setClientConnection(this);
        fileInformation fileInformation = new fileInformation(nameField.getText());
        System.out.println(nameField.getText());
        serverConnect = new ServerConnect(3333, main, this,fileInformation);
        main.showMainScene();
    }
}
