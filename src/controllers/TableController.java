package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import models.Contact;
import services.DBManager;
import services.ModalService;
import services.DataService;
import java.io.IOException;

/**
 * Controller which responsible for table view.
 */
public class TableController {
    // Scenes and stages.
    private Parent formModal, fileChooserModal;
    private Stage modalForm, modalFileChooser;

    // Controllers for the stages.
    private FormController formModalController;
    private SerializationController fileController;

    // Buttons that should have been disabled when nothing selected.
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private MenuItem editMenu;
    @FXML private MenuItem deleteMenu;

    @FXML private TextField search;
    @FXML private TableView<Contact> table;

    /**
     * Search method.
     * Executes from button search action or if enter is pressed.
     */
    private void search() {
        if (search.getText().isEmpty()) {
            table.setItems(DataService.contacts);
        } else {
            DataService.search(search.getText());
            table.setItems(DataService.filteredContacts);
        }
    }

    /**
     * Shows modal with form for adding new contact or editing existing.
     * @param title - title for the modal window.
     */
    private void showFormModal(String title) {
        if (modalForm == null)
            modalForm = ModalService.createStage(formModal);
        modalForm.setTitle(title);
        modalForm.showAndWait();
    }
    /**
     * Shows modal with file chooser for import/export data.
     * @param title - title for the modal window.
     */
    private void showFileChooser(String title) {
        if (modalFileChooser == null)
            modalFileChooser = ModalService.createStage(fileChooserModal);
        modalFileChooser.setTitle(title);
        modalFileChooser.showAndWait();
    }

    /**
     * Action on add button or menu add button.
     * Opens the window with form.
     */
    @FXML
    private void onButtonAdd() {
        Contact newContact = new Contact();
        formModalController.setFields(newContact);
        showFormModal("Добавление нового контакта");
        if (formModalController.getIsSaved())
            DataService.addContact(newContact);
    }

    /**
     * Action on edit button or menu edit button.
     * Opens the window with form.
     */
    @FXML
    private void onButtonEdit() {
        var contact = table.getSelectionModel().getSelectedItem();
        formModalController.setFields(contact);
        showFormModal("Редактирование контакта");
        if (formModalController.getIsSaved()) {
            if (!DBManager.getInstance("phonebookDB").editContact(contact, "phonebook")) {
                ModalService.showError("Error", "Something went wrong with database!");
                exit();
            }
        }
        table.refresh();
    }

    /**
     * Action on delete button or menu delete button.
     * Ask user if he confirms to delete.
     */
    @FXML
    private void onButtonDelete() {
        var result = ModalService.showConfirmAlert("Удаление контакта", "Нажмите Ок, чтобы удалить выбранный контакт");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DataService.removeContact(table.getSelectionModel().getSelectedItem());
            if (DataService.filteredContacts != null)
                DataService.filteredContacts.remove(table.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void onButtonSearch() {
        search();
    }

    @FXML
    private void onButtonAbout() {
        ModalService.showInfoAlert();
    }

    @FXML
    private void exit() {
        Platform.exit();
    }

    @FXML
    private void initialize() {
        // Set bindings by the selected items.
        deleteButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        deleteMenu.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        editButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        editMenu.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/FormModal.fxml"));
            formModal = loader.load();
            formModalController = loader.getController();

            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/FileChooser.fxml"));
            fileChooserModal = loader.load();
            fileController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }

        table.setItems(DataService.contacts);
    }

    @FXML
    private void onButtonImport() {
        showFileChooser("Импортировать контакты");
        if (fileController.getPath() != null)
            DataService.deserialize(fileController.getPath());
    }

    @FXML
    private void onButtonExport() {
        showFileChooser("Экспортировать контакты");
        if (fileController.getPath() != null)
            DataService.serialize(fileController.getPath());
    }

    @FXML
    private void onEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            search();
    }
}
