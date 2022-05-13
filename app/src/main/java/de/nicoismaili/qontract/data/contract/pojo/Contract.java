package de.nicoismaili.qontract.data.contract.pojo;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import de.nicoismaili.qontract.data.contract.ContractConverters;

/**
 * Contract entity containing Room information to
 * create contract rows in the database.
 *
 * @author Nico Ismaili
 */
@Entity(tableName = "contracts")
@TypeConverters({ContractConverters.class})
public class Contract implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contract_id")
    private int id;
    @ColumnInfo(name = "signed")
    private boolean isSigned;
    @ColumnInfo(name = "read")
    private boolean isRead;
    private long date;
    @NonNull
    private String location;
    @NonNull
    @ColumnInfo(name = "model_first_name")
    private String modelFirstname;
    @NonNull
    @ColumnInfo(name = "model_last_name")
    private String modelLastname;
    private String modelAddress;
    private String modelPhone;
    private String modelEmail;
    @ColumnInfo(name = "images_json")
    // Contains paths to the images belonging to a contract
    private List<String> images;
    // Contains the entire contract as an html string
    private String contractHTML;
    // Contains the signature of the model as an SVG
    private Bitmap modelSignature;

    public Contract() {
        location = "";
        modelLastname = "";
        modelFirstname = "";
        date = new Date().getTime();
    }

    @NonNull
    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", isSigned=" + isSigned +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", modelFirstname='" + modelFirstname + '\'' +
                ", modelLastname='" + modelLastname + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSigned() {
        return isSigned;
    }

    public void setSigned(boolean signed) {
        isSigned = signed;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    public void setLocation(@NonNull String location) {
        this.location = location;
    }

    @NonNull
    public String getModelFirstname() {
        return modelFirstname;
    }

    public void setModelFirstname(@NonNull String modelFirstname) {
        this.modelFirstname = modelFirstname;
    }

    @NonNull
    public String getModelLastname() {
        return modelLastname;
    }

    public void setModelLastname(@NonNull String modelLastname) {
        this.modelLastname = modelLastname;
    }

    public String getModelAddress() {
        return modelAddress;
    }

    public void setModelAddress(String modelAddress) {
        this.modelAddress = modelAddress;
    }

    public String getModelPhone() {
        return modelPhone;
    }

    public void setModelPhone(String modelPhone) {
        this.modelPhone = modelPhone;
    }

    public String getModelEmail() {
        return modelEmail;
    }

    public void setModelEmail(String modelEmail) {
        this.modelEmail = modelEmail;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getContractHTML() {
        return contractHTML;
    }

    public void setContractHTML(String contractHTML) {
        this.contractHTML = contractHTML;
    }

    public Bitmap getModelSignature() {
        return modelSignature;
    }

    public void setModelSignature(Bitmap modelSignature) {
        this.modelSignature = modelSignature;
    }
}
