//haven't done anything with it :(
package Server;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Downloads {

    @FXML
    private TableView<?> downloadTable;

    @FXML
    private TableColumn<?, ?> downloadFIleName;

    @FXML
    private TableColumn<?, ?> downloadFileSize;

    @FXML
    private TableColumn<?, ?> downloadProgress;

    @FXML
    private TableColumn<?, ?> downloadServer;
}
