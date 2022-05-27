package Server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class publicFileList {
    Main main;
    Socket socket;
    fileListThread fileListThread;
    public String fileName;
    private String saveFileName;
    public static ObservableList<files> serverFiles = FXCollections.observableArrayList();
    public void setSocket(Socket socket){
        System.out.println("Called Set Socket");
        this.socket = socket;
    }
    public String getFileName(){ return saveFileName; }
    public void setMain(Main main) {
        this.main = main;
    }

    public void setFileListThread(fileListThread fileListThread) {
        this.fileListThread = fileListThread;
    }

    @FXML
    private TableView<files> list;

    @FXML
    private TableColumn<files, String> listName;

    @FXML
    private TableColumn<files, Date> listDate;

    @FXML
    private TableColumn<files, String> listType;

    @FXML
    private TableColumn<files, Double> listSize;

    @FXML
    public void initialize() {
        listName.setCellValueFactory(new PropertyValueFactory<files, String>("fileName"));
        listDate.setCellValueFactory(new PropertyValueFactory<files, Date>("dateModified"));
        listSize.setCellValueFactory(new PropertyValueFactory<files, Double>("size"));
        listType.setCellValueFactory(new PropertyValueFactory<files, String>("type"));
        list.setItems(serverFiles);
    }

    void CLICK() {
        ObservableList<Node> cells = list.getChildrenUnmodifiable();
        for (int i = 0; i < cells.size(); i++) {

            cells.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getClickCount() == 2) {
                        int k = list.getSelectionModel().getSelectedIndex();
                        fileName = serverFiles.get(k).getFileName();
                        System.out.println(fileName);
                        System.out.println("am I here?");
                        double fileSize = serverFiles.get(k).getSizeInBytes();
                        try {
                            System.out.println("Going in");
                            while(socket == null) System.out.print("");
                            System.out.println("Got the socket");
                            FileChooser fileChooser = new FileChooser();
                            File file = fileChooser.showSaveDialog(main.stage);
                            saveFileName = file.getAbsolutePath();
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            System.out.println("socket is not null i hope");
                            oos.writeObject(fileName);
                            System.out.println("file writing");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}