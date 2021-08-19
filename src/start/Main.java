package start;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.DataService;
import services.DBManager;
import services.ModalService;

public class Main extends Application {
    private DBManager dbManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            String nameDB = "phonebookDB";
            String tableName = "phonebook";
            dbManager = DBManager.getInstance(nameDB);
            dbManager.createTable(tableName);
            DataService.contacts = FXCollections.observableArrayList(dbManager.getContacts(tableName));
        } catch (Exception e) {
            // Handling some errors connecting with database problems (i.e. db files crashed...).
            ModalService.showError("Error with database!", "Delete folder with database!");
            Platform.exit();
        }

        Parent root = FXMLLoader.load(getClass().getResource("/views/Table.fxml"));

        primaryStage.setTitle("Phonebook");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(550);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Release resources before exit.
        dbManager.releaseResources();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
