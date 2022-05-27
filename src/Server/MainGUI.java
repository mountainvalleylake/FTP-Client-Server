package Server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class MainGUI {

    public String path;
    public Button show;
    Main main;

    @FXML
    private Button availableButton;

    @FXML
    private Button directorySelect;

    @FXML
    private TextField directory;

    @FXML
    private ToggleButton fileButton;

    @FXML
    private ToggleButton contactButton;

    @FXML
    private ToggleButton historyButton;

    @FXML
    private Button downloadsButton;

    @FXML
    public TableView<files> filePublic;

    @FXML
    public TableColumn<files, String> filePublicName;

    @FXML
    public TableColumn<files, String> filePublicDate;

    @FXML
    public TableColumn<files, String> filePublicType;

    @FXML
    public TableColumn<files, String> filePublicSize;

    @FXML
    private ListView<String> contactList;

    @FXML
    public TableView<files> historyPublic;

    @FXML
    public TableColumn<files, String> historyPublicFile;

    @FXML
    public TableColumn<files, String> historyPublicSize;

    @FXML
    public TableColumn<files, String> historyPublicDate;

    @FXML
    public TableColumn<files, String> historyPublicServer;

    @FXML
    public TabPane fileTab;

    @FXML
    public AnchorPane contactAnchor;

    @FXML
    public TabPane historyTab;

    @FXML
    void availableButtonFired(ActionEvent event) {
        main.showAvailablePage();
    }

    void setMain(Main main) {
        this.main = main;
    }

    public void setContactList(ObservableList<String> serverName) { contactList.setItems(serverName);}
    @FXML
    public void initialize() {
        filePublicName.setCellValueFactory(new PropertyValueFactory<files, String>("fileName"));
        filePublicDate.setCellValueFactory(new PropertyValueFactory<files, String>("dateModified"));
        filePublicSize.setCellValueFactory(new PropertyValueFactory<files, String>("size"));
        filePublicType.setCellValueFactory(new PropertyValueFactory<files, String>("type"));
    }

    @FXML
    public void historyInit()
    {
        historyPublicFile.setCellValueFactory(new PropertyValueFactory<files, String>("fileDownloadName"));
        historyPublicDate.setCellValueFactory(new PropertyValueFactory<files, String>("fileDownloadDate"));
        historyPublicSize.setCellValueFactory(new PropertyValueFactory<files, String>("fileDownloadSize"));
        historyPublicServer.setCellValueFactory(new PropertyValueFactory<files, String>("serverName"));
    }

    public ObservableList<String> contacts = FXCollections.observableArrayList();
    static ObservableList<files> tableList = FXCollections.observableArrayList();
    public ObservableList<files> historyTable = FXCollections.observableArrayList();
    List<files> tableListArray = new ArrayList<>();

    @FXML
    void directorySelectFired(ActionEvent event) {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(main.stage);
            path = selectedDirectory.getAbsolutePath();
            System.out.println(path);
            directory.setText(path);
            new fileListThread(this, main, selectedDirectory);
        }
        catch (Exception ignored)
        {
        }
    }

    @FXML
    void downloadsButtonFired(ActionEvent event) {
        main.showDownloadPage();
    }

    @FXML
    void fileButtonFired(ActionEvent event) {
        if(fileButton.isSelected()) {
            fileTab.setVisible(true);
            contactAnchor.setVisible(false);
            historyTab.setVisible(false);
            contactButton.setSelected(false);
            historyButton.setSelected(false);
        }
        else{
            fileTab.setVisible(false);
            contactAnchor.setVisible(false);
            historyTab.setVisible(false);
            fileButton.setSelected(false);
            contactButton.setSelected(false);
            historyButton.setSelected(false);
        }
    }

    @FXML
    void historyButtonFired(ActionEvent event) {
        if(historyButton.isSelected()) {
            contactAnchor.setVisible(false);
            historyTab.setVisible(true);
            fileTab.setVisible(false);
            contactButton.setSelected(false);
            fileButton.setSelected(false);
        }
        else{
            fileTab.setVisible(false);
            contactAnchor.setVisible(false);
            historyTab.setVisible(false);
            fileButton.setSelected(false);
            contactButton.setSelected(false);
            historyButton.setSelected(false);
        }
    }

    @FXML
    void contactButtonFired(ActionEvent event) {
        if(contactButton.isSelected()) {
            contactAnchor.setVisible(true);
            historyTab.setVisible(false);
            fileTab.setVisible(false);
            historyButton.setSelected(false);
            fileButton.setSelected(false);
        }
        else{
            fileTab.setVisible(false);
            contactAnchor.setVisible(false);
            historyTab.setVisible(false);
            fileButton.setSelected(false);
            contactButton.setSelected(false);
            historyButton.setSelected(false);
        }
    }
}