package Client;

import Server.Main;
import Server.MainGUI;

import java.io.*;
import java.net.Socket;


public class FileServer implements Runnable{
    Main main;
    MainGUI mainGUI;
    String path;
    String fileName;
    Socket socket;
    Thread thread;
    public FileServer(Main main, String fileName, Socket socket){
        this.main = main;
        this.fileName = fileName;
        this.socket = socket;
        mainGUI = main.mainGUI;
        path = mainGUI.path;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try{
            OutputStream oS = socket.getOutputStream();
            File transferFile = new File (path + "\\" + fileName);
            System.out.println(transferFile.getAbsolutePath());
            InputStream iS = new FileInputStream(transferFile);

            int line = iS.read();
            oS.write(line);

            while(line > -1)
            {
                line = iS.read();
                System.out.println(line + " ");
                oS.write(line);
                oS.flush();
            }
            System.out.println("Sent");

            oS.close();
            iS.close();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}