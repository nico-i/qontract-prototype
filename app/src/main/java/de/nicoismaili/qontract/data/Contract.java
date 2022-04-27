package de.nicoismaili.qontract.data;

import java.io.Serializable;

/**
 * Contract POJO containing details about a contract
 *
 * @author Nico Ismaili
 */
public class Contract implements Serializable {

    private final long id;
    private boolean isSigned;
    private boolean isRead;
    private Model modelDetails;
    private final boolean[] qrCode;
    private byte[] modelSignature;

    public Contract(long id) {
        this.id = id;
        this.isSigned = false;
        this.isRead = false;
        this.modelDetails = null;
        this.modelSignature = new byte[256];
        this.qrCode = new boolean[256];
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", isSigned=" + isSigned +
                ", isRead=" + isRead +
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

    public byte[] getModelSignature() {
        return modelSignature;
    }

    public void setModelSignature(byte[] modelSignature) {
        this.modelSignature = modelSignature;
    }

    public long getId() {
        return id;
    }

    public boolean[] getQrCode() {
        return qrCode;
    }
}
