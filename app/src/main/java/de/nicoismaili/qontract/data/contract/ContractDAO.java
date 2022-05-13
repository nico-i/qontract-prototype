package de.nicoismaili.qontract.data.contract;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.nicoismaili.qontract.data.contract.pojo.Contract;

@Dao
public interface ContractDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContract(Contract contract);

    @Update
    void updateContract(Contract contract);

    @Delete
    void deleteContract(Contract contract);

    @Delete
    void deleteContracts(List<Contract> contract);

    @Query("SELECT * FROM contracts WHERE contract_id == :id")
    LiveData<Contract> getContractById(int id);

    @Query("SELECT * FROM contracts ORDER BY date DESC")
    LiveData<List<Contract>> getAllContractsMinSortedByDate();

    @Query("SELECT * FROM contracts WHERE model_first_name || ' ' || model_last_name LIKE :query")
    LiveData<List<Contract>> getAllContractsByQueryMinSortedByDate(String query);

    @Query("DELETE FROM contracts")
    void deleteAll();

}

