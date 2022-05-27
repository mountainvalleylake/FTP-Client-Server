package Server;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class fileListThread implements Runnable, Serializable {
    MainGUI mainGUI;
    Main main;
    Thread thread;
    File direct;

    fileListThread(MainGUI mainGUI, Main main, File direct)
    {
        this.mainGUI = mainGUI;
        this.main = main;
        main.setfileListThread(this);
        thread = new Thread(this);
        thread.start();
        this.direct = direct;
    }

    @Override
    public void run() {
        File[] crack = direct.listFiles();
        if(!MainGUI.tableList.isEmpty()) MainGUI.tableList.clear();
        for (File blink : crack) {
            files file = new files(blink);
            MainGUI.tableList.add(file);
        }

        mainGUI.filePublic.setItems(MainGUI.tableList);
        for(int i = 0; i < MainGUI.tableList.size(); i++) {mainGUI.tableListArray.add(i, MainGUI.tableList.get(i));}
        System.out.println("Added");
    }
}

