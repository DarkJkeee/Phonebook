package services;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Class to create stages and show alerts.
 */
public class ModalService {
    
    /**
     * Show error modal window with title and message.
     * @param title - title of the window.
     * @param message - message.
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Create a new stage with root.
     * @param root - parent root of the stage.
     * @return created stage.
     */
    public static Stage createStage(Parent root) {
        Stage stage = new Stage();
        stage.setMinHeight(150);
        stage.setMinWidth(300);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    /**
     * Shows info alert about me ;)
     */
    public static void showInfoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Бурштейн Глеб | БПИ192 | Мафиозник | Голубоглазый Молдован ;) |");
        alert.setContentText("Сидел три по пятерику");
        alert.showAndWait();
    }

    /**
     * Confirmation alert.
     * @param header - header text.
     * @param text - main text.
     * @return type of the tapped button.
     */
    public static Optional<ButtonType> showConfirmAlert(String header, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setContentText(text);
        alert.setHeaderText(header);
        return alert.showAndWait();
    }
}
