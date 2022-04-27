package de.nicoismaili.qontract.data.models;

import java.io.Serializable;

/**
 * Settings POJO containing settings key value pairs
 *
 * @author Nico Ismaili
 */
public class Settings implements Serializable {

    private final long id;
    private String firstname;
    private String lastname;
    private String address;
    private String email;
    private String phoneNumber;
    private boolean isModelMode;

    public Settings(long id, boolean isModelMode, String firstname, String lastname, String address, String email, String phoneNumber) {
        this.id = id;
        this.isModelMode = isModelMode;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isModelMode() {
        return isModelMode;
    }

    public void setModelMode(boolean modelMode) {
        isModelMode = modelMode;
    }

    public long getId() {
        return id;
    }
}
