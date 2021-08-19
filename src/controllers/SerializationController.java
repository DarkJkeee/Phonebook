package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Controller which responsible for import/export views.
 */
public class SerializationController {

    @FXML
    private TextField textField;
    private File file;

    /**
     * Open file chooser or save dialog.
     * @param actionEvent event which define a type of window (import/export).
     */
    @FXML
    private void openFileChooser(MouseEvent actionEvent) {

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        if (stage.getTitle().equals("Импортировать контакты")) {
            fileChooser.setTitle("Выберите файл");
            file = fileChooser.showOpenDialog(stage);
        } else {
            fileChooser.setTitle("Выберите папку");
            file = fileChooser.showSaveDialog(stage);
        }

        textField.setText(getPath());
    }

    public String getPath() {
        if (file != null)
            return file.getAbsolutePath();
        return null;
    }

    /**
     * Close the window.
     * @param actionEvent helps to close the current window.
     */
    @FXML
    private void onButtonOk(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Close the window and set file = null.
     * @param actionEvent helps to close the current window.
     */
    @FXML
    private void onButtonCancel(ActionEvent actionEvent) {
        file = null;
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
