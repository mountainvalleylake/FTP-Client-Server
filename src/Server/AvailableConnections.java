package Server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class AvailableConnections {
    Main main;
    ServerConnect serverConnect;
    editListener editListener;
    String[] addresses = new String[10];
    int[] portsAsServer = new int[10];

    @FXML
    public ListView<String> availableList;

    void setMain(Main main) {
        this.main = main;
    }

    void setServer(ServerConnect serverConnect) {
        this.serverConnect = serverConnect;
    }

    void setEditListener(editListener editListener){ this.editListener = editListener; }

    ObservableList<String> observableList = FXCollections.observableArrayList();

    void setConnected(String[] e) {
        for (int i = 0; e[i] != null; i++) {
            observableList.add(e[i]);
        }
        availableList.setItems(observableList);
    }


    void CLICK()
    {
        ObservableList<Node> cells = availableList.getChildrenUnmodifiable();
        for(int i = 0; i < cells.size(); i++) {
            cells.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                    public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2)
                    {
                        String serverName = availableList.getSelectionModel().getSelectedItem();
                        String name = addresses[availableList.getSelectionModel().getSelectedIndex()];
                        System.out.println(name);
                        String serverAddress = addresses[availableList.getSelectionModel().getSelectedIndex()];
                        int portAsServer = portsAsServer[availableList.getSelectionModel().getSelectedIndex()];
                        System.out.println();
                        GetList getList = new GetList(main,editListener,serverName,serverAddress, serverConnect, portAsServer);
                        while (!getList.gotList) System.out.print("");
                        main.showFiles("File List");
                    }
                }
            });
        }
    }

}

