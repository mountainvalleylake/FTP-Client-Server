package CentralServerPackage;

import FileInfoPackage.clientInfo;
import FileInfoPackage.fileInformation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

class CentralServer implements Runnable{
    Socket socket;
    ServerSocket serverSocket;
    Thread thread;
    int port;
    CentralServer(int port){
        this.port = port;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        int i = 0;
        try{
            serverSocket = new ServerSocket(port);
            while(true){
                socket = serverSocket.accept();
                System.out.println("new connection");
                new Broadcast(socket);
    }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Broadcast implements Runnable{
    Socket socket;
    static ArrayList<Socket> socketArrayList= new ArrayList<>();
    static ArrayList<clientInfo> Client = new ArrayList<>();
    Thread thread;
    Broadcast(Socket socket)
    {
        this.socket = socket;
        socketArrayList.add(socket);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String address1 = String.valueOf(socket.getInetAddress());
            StringTokenizer tokens = new StringTokenizer(address1, "/");
            String address = tokens.nextToken();
            System.out.println(address);

            fileInformation fileInformation = (fileInformation) objectInputStream.readObject();
            String strings = fileInformation.getUserName();
            int portAsServer = fileInformation.portAsServer;
            clientInfo clientInfo = new clientInfo(strings, address, portAsServer);
            Client.add(clientInfo);
            for (int j = 0; j < socketArrayList.size(); j++) {
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketArrayList.get(j).getOutputStream());
                    objectOutputStream.writeObject(Client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    }

public class Accepting {
    public static void main(String[] args) {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            System.out.println("Central Server IP: " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        new CentralServer(3333);
    }
}
