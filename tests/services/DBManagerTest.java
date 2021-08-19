package services;

import models.Contact;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DBManagerTest {
    static DBManager dbManager;
    static Contact contact1, contact2, contact3;

    @BeforeAll
    static void setup() {
        dbManager = DBManager.getInstance("memory:testDB");
        var tables = List.of("tableForSelectAll", "tableForInsert", "tableForRemove", "tableForEdit", "tableForSearch");

        contact1 = new Contact();
        contact2 = new Contact();
        contact3 = new Contact();
        contact1.setFirstName("Gleb");
        contact1.setLastName("Burstein");
        contact1.setMobilePhone("+79996666666");
        contact1.setNotes("I love pizza!!");
        contact2.setFirstName("Another");
        contact2.setLastName("Gleb");
        contact2.setMobilePhone("+79996666666");
        contact3.setFirstName("Third");
        contact3.setLastName("One");
        contact3.setMobilePhone("+79996666666");

        tables.forEach(DBManagerTest::setupContacts);
    }

    @AfterAll
    static void end() {
        dbManager.releaseResources();
    }

    static void setupContacts(String tableName) {
        dbManager.createTable(tableName);
        dbManager.addContact(contact1, tableName);
        dbManager.addContact(contact2, tableName);
        dbManager.addContact(contact3, tableName);
    }

    @Test
    void getContacts() {
        var contacts = dbManager.getContacts("tableForSelectAll");
        assertEquals(contact1.getFirstName(), contacts.get(0).getFirstName());
        assertEquals(contact2.getFirstName(), contacts.get(1).getFirstName());
        assertEquals(contact3.getFirstName(), contacts.get(2).getFirstName());
    }

    @Test
    void addContact() {
        var contact4 = new Contact();
        contact4.setFirstName("Hey!");
        contact4.setLastName("I am new here!");
        contact4.setMobilePhone("+79996666666");
        dbManager.addContact(contact4, "tableForInsert");
        assertEquals(4, dbManager.getContacts("tableForInsert").size());
    }

    @Test
    void removeContact() {
        if (dbManager.removeContact(contact3, "tableForRemove"))
            assertEquals(2, dbManager.getContacts("").size());
    }

    @Test
    void editContact() {
        var editableContact = new Contact();
        editableContact.setFirstName("I am new here!");
        editableContact.setLastName("Yeah");
        editableContact.setMobilePhone("+77777777777");
        editableContact = dbManager.addContact(editableContact, "tableForEdit");
        editableContact.setNotes("Now i have some notes!");
        dbManager.editContact(editableContact, "tableForEdit");

        var contact = dbManager.getContact(editableContact.getFirstName(), editableContact.getLastName(),
                editableContact.getMiddleName(), "tableForEdit");

        assertEquals("Now i have some notes!", contact.getNotes());
    }

    @Test
    void search() {
        var contacts = dbManager.search("Gleb", "tableForSearch");
        assertEquals(2, contacts.size());
    }
}