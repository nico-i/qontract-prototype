package de.nicoismaili.qontract.data.contract.pojo;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.Date;

import de.nicoismaili.qontract.data.contract.ContractConverters;

@TypeConverters({ContractConverters.class})
public class ContractMin {
    @ColumnInfo(name = "contract_id")
    private int id;
    @ColumnInfo(name = "signed")
    private boolean isSigned;
    private Date date;
    private String location;
    @ColumnInfo(name = "model_first_name")
    private String modelFirstname;
    @ColumnInfo(name = "model_last_name")
    private String modelLastname;

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

    public String getModelFirstname() {
        return modelFirstname;
    }

    public void setModelFirstname(String modelFirstname) {
        this.modelFirstname = modelFirstname;
    }

    public String getModelLastname() {
        return modelLastname;
    }

    public void setModelLastname(String modelLastname) {
        this.modelLastname = modelLastname;
    }
}
