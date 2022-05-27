package Client;

import Server.Main;
import Server.files;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

public class FileClient implements Runnable {
    Main main;
    Socket socket;
    String name;
    String serverName;
    public FileClient(Socket socket, String name, Main main, String serverName){
        this.socket = socket;
        this.main = main;
        this.name = name;
        this.serverName = serverName;
        Thread thread = new Thread(this);
        thread.start();
    }
    @Override
    public void run() {
        InputStream is;
        try {
            is = socket.getInputStream();
            FileOutputStream oS = new FileOutputStream(name);
            System.out.println(name);
            System.out.println("file output stream built");
            long size = 1;
            int line = is.read();
            oS.write(line);

            while(line > -1)
            {
                size++;
                line = is.read();
                oS.write(line);
            }
            main.mainGUI.contacts.add(serverName);
            main.mainGUI.setContactList(main.mainGUI.contacts);
            System.out.println("Received!!!");

            files files = new files(new File(name));
            files.setDateDownloaded(LocalDate.now());
            main.mainGUI.historyTable.add(files);
            main.mainGUI.historyPublic.setItems(main.mainGUI.historyTable);

            is.close();
            oS.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}