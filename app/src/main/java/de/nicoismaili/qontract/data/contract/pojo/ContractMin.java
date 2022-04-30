package de.nicoismaili.qontract.data.contract.pojo;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.Date;

import de.nicoismaili.qontract.data.contract.ContractConverters;

@TypeConverters({ContractConverters.class})
public class ContractMin {
    @ColumnInfo(name = "contract_id")
    public int id;
    @ColumnInfo(name = "signed")
    public boolean isSigned;
    public Date date;
    public String location;
    @ColumnInfo(name = "model_first_name")
    public String modelFirstname;
    @ColumnInfo(name = "model_last_name")
    public String modelLastname;
}
