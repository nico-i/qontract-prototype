package de.nicoismaili.qontract.data.models;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Contract POJO containing details about a contract
 *
 * @author Nico Ismaili
 */
public class Contract implements Serializable {

    public final static String IMAGE_TYPE_QR = "QR_CODE";
    public final static String IMAGE_TYPE_SIGNATURE = "SIGNATURE";
    public final static String IMAGE_TYPE_PHOTO = "PHOTO";

    private final long id;
    private boolean isSigned;
    private boolean isRead;
    private Date date;
    private String location;
    private Model modelDetails;
    // Images will be stored in Base64 format on the database
    private ArrayList<Bitmap> images;
    private Bitmap qrCode;
    private Bitmap modelSignature;



    public Contract() {
        this.id = -1;
        this.date = new Date(System.currentTimeMillis());
        this.location = "";
        this.isSigned = false;
        this.isRead = false;
        this.modelDetails = null;
        this.modelSignature = null;
        this.qrCode = null;
        this.images = new ArrayList<>();
    }

    public Contract(long id, boolean isSigned, boolean isRead, Date date, String location, Model modelDetails) {
        this.id = id;
        this.isSigned = isSigned;
        this.isRead = isRead;
        this.date = date;
        this.location = location;
        this.modelDetails = modelDetails;
        this.images = null;
        this.qrCode = null;
        this.modelSignature = null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", isSigned=" + isSigned +
                ", isRead=" + isRead +
                ", modelDetails=" + modelDetails +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contract contract = (Contract) o;

        return id == contract.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public long getId() {
        return id;
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

    public Model getModelDetails() {
        return modelDetails;
    }

    public void setModelDetails(Model modelDetails) {
        this.modelDetails = modelDetails;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public Bitmap getModelSignature() {
        return modelSignature;
    }

    public void setModelSignature(Bitmap modelSignature) {
        this.modelSignature = modelSignature;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }

    public Bitmap getQrCode() {
        return qrCode;
    }

    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }
}
