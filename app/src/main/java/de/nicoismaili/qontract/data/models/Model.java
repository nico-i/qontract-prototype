package de.nicoismaili.qontract.data.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Model POJO containing details about a model
 *
 * @author Nico Ismaili
 */
public class Model implements Serializable {

    private String firstname;
    private String lastname;
    private String address;
    private String phone;
    private String email;

    public Model(String firstname, String lastname, String address) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
    }

    public Model(String firstname, String lastname, String address, String phone, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    /**
     * This constructor is used for creating a 'stripped'
     * model object, which has only the necessary data to
     * represent it in an overview.
     *
     * @param modelFirstName first name of the model
     * @param modelLastName  last name of the model
     */
    public Model(String modelFirstName, String modelLastName) {
        this.firstname = modelFirstName;
        this.lastname = modelLastName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Model{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
