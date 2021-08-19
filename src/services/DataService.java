package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Contact;
import services.validation.ValidatorService;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service which responsible for data and import/export/search processes.
 * Works with database manager.
 */
public class DataService {
    // Services.
    private static final DBManager dbManager = DBManager.getInstance("phonebookDB");
    private static final ValidatorService validatorService = new ValidatorService();
    private static final String tableName = "phonebook";

    // Main contacts.
    public static ObservableList<Contact> contacts, filteredContacts;

    /**
     * Inserts element to database and main list.
     * @param contact - new contact.
     */
    public static void addContact(Contact contact) {
        var newContact = dbManager.addContact(contact, tableName);
        if (newContact != null)
            contacts.add(newContact);
    }

    /**
     * Removes element from database and lists.
     * @param contact - target contact.
     */
    public static void removeContact(Contact contact) {
        if (dbManager.removeContact(contact, tableName))
            contacts.remove(contact);
    }

    /**
     * Filters data.
     * @param query - query string.
     */
    public static void search(String query) {
        filteredContacts = contacts.stream().filter(contact -> dbManager.search(query, tableName).contains(contact.getID()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Method which serialize current data.
     * @param path - path to serialize.
     */
    public static void serialize(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(contacts));
            oos.close();
        } catch (IOException e) {
            ModalService.showError("Произошла ошибка с файлом", "Не получилось сериализовать данные!");
        }
    }

    /**
     * Method which deserialize data and put it in contacts.
     * @param path - path to deserialize.
     */
    public static void deserialize(String path) {

        ObservableList<Contact> deserializeContacts = null;
        try {
            FileInputStream fos = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fos);
            List<Contact> list = (List<Contact>) ois.readObject();
            deserializeContacts = FXCollections.observableList(list);
        } catch (IOException | ClassNotFoundException ignored) {}

        if (deserializeContacts != null) {
            Iterator<Contact> iteratorNewContacts = deserializeContacts.iterator();

            while (iteratorNewContacts.hasNext()) {
                var newContact = iteratorNewContacts.next();
                var newId = newContact.getFirstName() + " " + newContact.getLastName() + " " + newContact.getMiddleName();

                validatorService.setContact(newContact);
                var errors = validatorService.validateFields(newContact.getFirstName(), newContact.getMiddleName(), newContact.getLastName(), newContact.getMobilePhone(), newContact.getHomePhone());

                if (errors != null) {
                    iteratorNewContacts.remove();
                    ModalService.showError("Данный контакт: " + newId + " некорректнен!", errors.getMessage());
                }
            }

            for (var contact : deserializeContacts) {
                addContact(contact);
            }
        }
    }

}