package models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Model of contact.
 */
public class Contact implements Serializable {
    // Fields.
    private int ID;
    private String firstName;
    private String lastName;
    private String middleName;
    private String mobilePhone;
    private String homePhone;
    private String address;
    private LocalDate dateOfBirth;
    private String notes;

    public Contact() {
        firstName = "";
        lastName = "";
        middleName = "";
        mobilePhone = "";
        homePhone = "";
        address = "";
        dateOfBirth = null;
        notes = "";
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() {
        return ID;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getMobilePhone() {
        return mobilePhone;
    }
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
    public String getHomePhone() {
        return homePhone;
    }
    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

}
