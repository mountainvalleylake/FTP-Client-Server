package Server;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class files implements Serializable{
    private File file;
    private String fileName;
    private String dateModified;
    private String type;
    private LocalDate dateDownloaded;
    private double tempSize;
    private String size;
    int sizeToken;

    public files(File file) {
        this.file = file;
        setFileName();
        setDateModified();
        setType();
        setSize();
    }

    public void setDateDownloaded(LocalDate dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName() {
        fileName = file.getName();
    }

    public void setDateModified() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        dateModified = simpleDateFormat.format(file.lastModified());
    }

    public String getExtension() {
        if (fileName == null) {
            return null;
        }
        int extensionPos = fileName.lastIndexOf('.');
        int lastSeparator = fileName.lastIndexOf('\\');
        int index = lastSeparator > extensionPos ? -1 : extensionPos;
        if (index == -1) {
            return "File Folder";
        } else {
            return fileName.substring(index);
        }
    }

    public void setType() {
        type = getExtension();
    }

    public void setSize() {
        double temp = 0, len = file.length();
        if (len < 1 || (len >= 1 && len < 1024)) {
            sizeToken = 0;
            temp = len;
        } else if (len >= 1024 && len < 1048576) {
            sizeToken = 1;
            temp = len / 1024;
        } else if (len >= 1048576 && len < 1073741824) {
            sizeToken = 2;
            temp = len / 1048576;
        } else if (len >= 1073741824) {
            sizeToken = 3;
            temp = len / 1073741824;
        }
        tempSize = Math.round(temp * 100.0) / 100.0;
        switch (sizeToken) {
            case 0:
                size = (tempSize + " bytes");
                break;
            case 1:
                size = (tempSize + " KB");
                break;
            case 2:
                size = (tempSize + " MB");
                break;
            case 3:
                size = (tempSize + " GB");
                break;
        }
    }

    public String getDateModified() {
        return dateModified;
    }

    public double getSizeInBytes() { return file.length(); }
    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }
}
