package services.validation;

import models.Contact;
import services.DataService;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Service which check text in text fields.
 */
public class ValidatorService {
    private Contact contact;  // Current contact.

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * Check if these values are correct.
     * @param firstName - first name of the contact.
     * @param middleName - middle name of the contact.
     * @param lastName - last name of the contact.
     * @param mobilePhone - mobile phone of the contact.
     * @param homePhone - home phone of the contact.
     * @return error or null if everything fine.
     */
    public ValidationError validateFields(String firstName, String middleName, String lastName, String mobilePhone, String homePhone) {
        var errorInfo = "";
        var errorsFields = new ArrayList<FailedField>();

        var id = firstName + lastName + middleName;
        for (var tableContact : DataService.contacts) {
            var otherId = tableContact.getFirstName() + tableContact.getLastName() + tableContact.getMiddleName();
            if (id.equals(otherId) && contact != tableContact) {
                errorsFields.add(FailedField.FIRSTNAME);
                errorsFields.add(FailedField.LASTNAME);
                errorsFields.add(FailedField.MIDDLENAME);
                errorInfo += "Данный контакт уже существует!\n";
                break;
            }
        }

        if (firstName.isBlank()) {
            errorsFields.add(FailedField.FIRSTNAME);
            errorInfo += "Имя является обязательным полем!\n";
        }

        if (lastName.isBlank()) {
            errorsFields.add(FailedField.LASTNAME);
            errorInfo += "Фамилия является обязательным полем!\n";
        }

        if (homePhone.isBlank() && mobilePhone.isBlank()) {
            errorsFields.add(FailedField.MOBILE_PHONE);
            errorsFields.add(FailedField.HOME_PHONE);
            errorInfo += "Один из номеров телефона является обязательным!\n";
        } else {
            if (!mobilePhone.isBlank() && checkPhoneFormat(mobilePhone)) {
                errorsFields.add(FailedField.MOBILE_PHONE);
                errorInfo += "Неправильный формат мобильного номера телефона! (российский формат - +7xxxxxxxxxx)\n";
            }
            if (!homePhone.isBlank() && checkPhoneFormat(homePhone)) {
                errorsFields.add(FailedField.HOME_PHONE);
                errorInfo += "Неправильный формат домашнего номера телефона! (российский формат - +7xxxxxxxxxx)\n";
            }
        }

        if (errorInfo.isEmpty())
            return null;
        else
            return new ValidationError(errorInfo, errorsFields);
    }

    /**
     * Checks phone with regex.
     * @param phone - phone number.
     * @return true if phone is incorrect, false otherwise.
     */
    private boolean checkPhoneFormat(String phone) {
        String regex = "^(\\+7|7|8)?[\\s\\-]?\\(?[0-9]{3}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$";
        return !Pattern.matches(regex, phone);
    }
}
