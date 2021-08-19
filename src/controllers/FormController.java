package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Contact;
import services.validation.ValidatorService;
import services.ModalService;

public class FormController {
    private final ValidatorService validatorService = new ValidatorService();

    private boolean isSaved = false;
    private Contact contact;

    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField middleName;
    @FXML private TextField mobilePhone;
    @FXML private TextField homePhone;
    @FXML private TextField address;
    @FXML private TextArea notes;
    @FXML private DatePicker dateOfBirth;

    private void changeColor(TextField textField) {
        textField.setStyle("-fx-text-box-border: #ff0000;");
    }

    private boolean checkFields() {
        clearFields();

        validatorService.setContact(contact);
        var error = validatorService.validateFields(firstName.getText(), middleName.getText(), lastName.getText(), mobilePhone.getText(), homePhone.getText());

        if (error != null) {
            for (var field : error.getFailedFields()) {
                switch (field) {
                    case FIRSTNAME: changeColor(firstName); break;
                    case LASTNAME: changeColor(lastName); break;
                    case MIDDLENAME: changeColor(middleName); break;
                    case HOME_PHONE: changeColor(homePhone); break;
                    case MOBILE_PHONE: changeColor(mobilePhone); break;
                }
            }
            ModalService.showError("Введены неправильные данные", error.getMessage());
            return false;
        }
        return true;
    }

    private void setContactFields(Contact contact) {
        contact.setFirstName(firstName.getText());
        contact.setLastName(lastName.getText());
        contact.setMiddleName(middleName.getText());
        contact.setMobilePhone(mobilePhone.getText());
        contact.setHomePhone(homePhone.getText());
        contact.setAddress(address.getText());
        contact.setDateOfBirth(dateOfBirth.getValue());
        contact.setNotes(notes.getText());
    }

    private void clearFields() {
        firstName.setStyle("");
        lastName.setStyle("");
        middleName.setStyle("");
        mobilePhone.setStyle("");
        homePhone.setStyle("");
    }

    public boolean getIsSaved() {
        return isSaved;
    }

    public void setFields(Contact contact) {
        clearFields();
        this.contact = contact;
        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        middleName.setText(contact.getMiddleName());
        mobilePhone.setText(contact.getMobilePhone());
        homePhone.setText(contact.getHomePhone());
        address.setText(contact.getAddress());
        dateOfBirth.setValue(contact.getDateOfBirth());
        notes.setText(contact.getNotes());
    }

    @FXML
    private void onButtonSave(ActionEvent actionEvent) {
        if (!checkFields())
            return;
        setContactFields(contact);
        isSaved = true;
        closeWindow(actionEvent);
    }

    @FXML
    private void onButtonCancel(ActionEvent actionEvent) {
        isSaved = false;
        closeWindow(actionEvent);
    }

    private void closeWindow(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
