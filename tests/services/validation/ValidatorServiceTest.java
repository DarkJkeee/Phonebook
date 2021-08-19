package services.validation;

import javafx.collections.FXCollections;
import models.Contact;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.DataService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorServiceTest {

    private static Contact contact1, contact2;
    ValidatorService validatorService = new ValidatorService();

    @BeforeAll
    static void start() {
        DataService.contacts = FXCollections.observableArrayList();
        contact2 = new Contact();
        contact2.setFirstName("Gleb");
        contact2.setLastName("");
        contact2.setMobilePhone("");
        contact2.setHomePhone("");

        contact1 = new Contact();
        contact1.setFirstName("Gleb");
        contact1.setLastName("");
        contact1.setMiddleName("");
        contact1.setMobilePhone("");
        contact1.setHomePhone("");
        contact1.setNotes("");
        contact1.setAddress("");
    }

    @Test
    void setContact() {
        validatorService.setContact(contact1);
    }

    @Test
    void validateFields() {
        var errors = validatorService.validateFields(contact1.getFirstName(), contact1.getMiddleName(), contact1.getLastName(), contact1.getMobilePhone(), contact1.getHomePhone());
        DataService.contacts.add(contact1);
        validatorService.setContact(contact2);
        var errors2 = validatorService.validateFields(contact2.getFirstName(), contact2.getMiddleName(), contact2.getLastName(), contact2.getMobilePhone(), contact2.getHomePhone());

        var failedFields = new ArrayList<FailedField>();
        failedFields.add(FailedField.LASTNAME);
        failedFields.add(FailedField.MOBILE_PHONE);
        failedFields.add(FailedField.HOME_PHONE);
        var failedFieldsTest = errors.getFailedFields();

        for (int i = 0; i < failedFieldsTest.size(); ++i)
            assertEquals(failedFields.get(i), failedFieldsTest.get(i));

        assertEquals("Данный контакт уже существует!\n" +
                "Фамилия является обязательным полем!\n" +
                "Один из номеров телефона является обязательным!\n", errors2.getMessage());
        assertEquals("Фамилия является обязательным полем!\n" +
                "Один из номеров телефона является обязательным!\n", errors.getMessage());

    }
}