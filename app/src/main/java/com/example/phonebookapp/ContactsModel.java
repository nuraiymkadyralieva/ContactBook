package com.example.phonebookapp;

public class ContactsModel {
    // fields for firstName, lastName, phoneNumber and id.
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String photoPath;
    private int id;

    // constructor
    public ContactsModel(String firstName, String lastName, String phoneNumber, String photoPath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.photoPath = photoPath != null ? photoPath : ""; // Устанавливаем пустую строку, если photoPath null
    }

    // creating getter and setter methods
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
