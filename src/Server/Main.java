package Server;

import Client.FileServer;
import FileInfoPackage.clientInfo;
import FileInfoPackage.fileInformation;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import Client.FileServer;
import Client.FileClient;

class ServerConnect implements Runnable{
    Hashtable onlines;
    Main main;
    int min = 3334;
    int max = 9000;
    int xrand;
    ClientConnection clientConnection;
    fileInformation fileInformation;
    int port;

    Thread t;
    Socket socket;
    int portAsServer = getRand(min, max);
    ServerConnect(int port, Main main, ClientConnection clientConnection, fileInformation fileInformation){
        this.port = port;
        this.main = main;
        this.clientConnection = clientConnection;
        this.fileInformation = fileInformation;
        fileInformation.portAsServer = portAsServer;
        t = new Thread(this);
        t.start();
    }
    int getRand(int min, int max){
        Random r = new Random();
        xrand= r.nextInt((max-min)+1) + min;
        return xrand;
    }
    @Override
    public void run() {
        try{
            String host = clientConnection.ipAddress.getText();
            socket = new Socket(host, port);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(fileInformation);

            new editListener(this,socket, main.availableConnections, main);
            }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class editListener implements Runnable{
    AvailableConnections availableConnections;
    ServerConnect serverConnect;
    Socket socket;
    ArrayList<clientInfo> editedClient = new ArrayList<>();
    Main main;
    editListener(ServerConnect serverConnect, Socket socket, AvailableConnections availableConnections, Main main){
        this.main = main;
        this.serverConnect = serverConnect;
        this.availableConnections = availableConnections;
        this.socket = socket;
        main.setEditListener(this);
        Thread thread = new Thread(this);
        thread.start();
    }
    @Override
    public void run() {
        try {
            new GiveList(main,this,serverConnect.xrand);
            while (true) {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                editedClient = (ArrayList<clientInfo>) objectInputStream.readObject();
                //Enumeration key = editedClient.keys();
                String[] names = new String[10];
                String[] address = new String[10];
                int[] portAsServer = new int[10];

                for (int j = 0; j < editedClient.size(); j++) names[j] = editedClient.get(j).userName;
                for (int j = 0; j < editedClient.size(); j++) address[j] = editedClient.get(j).address;
                for (int j = 0; j < editedClient.size(); j++) portAsServer[j] = editedClient.get(j).portAsServer;
                main.names = names;
                main.address= address;
                main.portAsServer = portAsServer;

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class GetList implements Runnable{
    Main main;
    editListener editListener;
    String serverName;
    String serverAddress;
    ServerConnect serverConnect;
    int portAsServer;
    boolean gotList = false;
    Thread t;

    GetList(Main main, editListener editListener,String serverName, String serverAddress, ServerConnect serverConnect, int portAsServer){
        this.main = main;
        this.editListener = editListener;
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.serverConnect=serverConnect;
        this.portAsServer = portAsServer;
        main.setGetList(this);
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(serverAddress, portAsServer);
            System.out.println(serverAddress + " meWmEwMew" + portAsServer);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            List<files> getArray = (List<files>) ois.readObject();
            System.out.println(getArray);
            ObservableList<files> got = FXCollections.observableArrayList();
            for(int i = 0; i < getArray.size(); i++) {got.add(i, getArray.get(i));}
            System.out.println(got);
            publicFileList.serverFiles = got;
            gotList = true;
            while (main.plist == null) System.out.print("");
            System.out.println("outside loop");
            main.plist.setSocket(socket);
            System.out.println("Set up" + !(socket==null));
            String name;
            while (main.plist.getFileName() == null) System.out.print("");
            System.out.println("Getting a name");
            name = main.plist.getFileName();
            System.out.println(name);
            new FileClient(socket,name,main, serverName);
            System.out.println("Created new FileClient, for better or worse...");
            //new FileClient()   //call file client's constructor
            //main.showFiles(serverName);
        } catch (UnknownHostException e){
            System.out.println(serverAddress+ "   " +portAsServer);
            System.out.println("Here I am!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class GiveList implements Runnable{
    Socket socket;
    fileListThread fileListThread;
    Main main;
    MainGUI mainGUI;
    editListener editListener;
    ServerSocket serverSocket;
    ArrayList<Socket> socketArray = new ArrayList<>();
    int portAsServer;

    GiveList(Main main, editListener editListener, int portAsServer){
        this.main = main;

        this.editListener = editListener;
        this.portAsServer = portAsServer;
        this.fileListThread = main.getfileListThread();
        Thread thread = new Thread(this);
        thread.start();
    }
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(portAsServer);//error
            System.out.println("Screw You!");
            System.out.println(portAsServer);
            while (true){
                socket = serverSocket.accept();
                System.out.println("You Brat!!!");
                socketArray.add(socket);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                mainGUI = main.mainGUI;
                oos.writeObject(mainGUI.tableListArray);
                System.out.println("Written");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String fileName = (String) ois.readObject();
                System.out.println(fileName+" Got it");
                new FileServer(main,fileName,socket);
        }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
public class Main extends Application {
    String[] names;
    String[] address;
    int[] portAsServer;
    public Stage stage;
    Stage downloadStage;
    public MainGUI mainGUI;
    int tester;
    ServerConnect serverConnect;
    ClientConnection clientConnection;
    AvailableConnections availableConnections;
    editListener editListener;
    GetList getList;
    GiveList giveList;
    publicFileList plist;
    String user;
    fileListThread flThread;
    void setGiveList(GiveList giveList){ this.giveList = giveList; }
    void setfileListThread(fileListThread flThread){ this.flThread = flThread; }
    fileListThread getfileListThread(){ return flThread;}
    void setGetList(GetList getList){ this.getList = getList; }
    void setClientConnection(ClientConnection clientConnection) { this.clientConnection = clientConnection; }
    void setServer(ServerConnect serverConnect) { this.serverConnect = serverConnect; }
    void setEditListener(editListener editListener){this.editListener = editListener;}

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        stage = primaryStage;
        showClientConnectPage();
    }
    @FXML
    public void showClientConnectPage()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("clientConnect.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClientConnection clientConnection = loader.getController();
        clientConnection.setMain(this);

        stage.setTitle("Connection");
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    public void showMainScene()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainGUI.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainGUI = loader.getController();
        mainGUI.setMain(this);
        mainGUI.initialize();
        mainGUI.historyInit();
        mainGUI.fileTab.setVisible(true);
        mainGUI.contactAnchor.setVisible(false);
        mainGUI.historyTab.setVisible(false);
        tester = 115698;

        stage.setTitle(clientConnection.nameField.getText());
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void showAvailablePage()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AvailableConnections.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        availableConnections = loader.getController();
        availableConnections.setMain(this);
        while (names == null) System.out.print("");
        availableConnections.setConnected(names);
        availableConnections.addresses = address;
        availableConnections.portsAsServer = portAsServer;

        availableConnections.setServer(serverConnect);
        availableConnections.setEditListener(editListener);

        Stage bStage = new Stage();
        bStage.setTitle("Online Connections");
        bStage.setScene(new Scene(root));
        bStage.show();
        availableConnections.CLICK();
    }



    @FXML
    public void showFiles(String user){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("publicFileList.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.user = user;
        plist = loader.getController();
        plist.initialize();
        plist.setMain(this);
        plist.setFileListThread(flThread);
        System.out.println("here");
        Stage cStage = new Stage();
        cStage.setTitle(user);
        cStage.setScene(new Scene(root));
        cStage.show();
        plist.CLICK();
    }
    @FXML
    public void showDownloadPage() {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("downloads.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadStage = new Stage();
        downloadStage.setTitle("Downloads");
        downloadStage.setScene(new Scene(root));
        downloadStage.show();
    }
    public static void main(String[] args) { launch(args);}
}
