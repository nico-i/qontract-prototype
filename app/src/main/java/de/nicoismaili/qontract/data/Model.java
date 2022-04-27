package de.nicoismaili.qontract.data;

import android.location.Address;

import java.io.Serializable;

/**
 * Model POJO containing details about a model
 *
 * @author Nico Ismaili
 */
public class Model implements Serializable {

    private final long id;
    private String firstname;
    private String lastname;
    private Address homeAddress;
    private String phoneNumber;
    private String email;

    public Model(int id, String firstname, String lastname, Address homeAddress) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.homeAddress = homeAddress;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Model model = (Model) o;

        return id == model.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
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

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }
}
