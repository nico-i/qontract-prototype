package de.nicoismaili.qontract.data.contract;

import android.app.Application;
import androidx.lifecycle.LiveData;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import java.util.List;

public class ContractRepository {
  private final ContractDAO contractDAO;

  public ContractRepository(Application application) {
    ContractRoomDatabase db = ContractRoomDatabase.getDatabase(application);
    contractDAO = db.contractDAO();
  }

  /**
   * Get a List of all Contracts currently in the database.
   *
   * @return List of all Contracts currently in the database
   */
  public LiveData<List<Contract>> getAllContracts() {
    return contractDAO.getAllContractsMinSortedByDate();
  }

  /**
   * Insert a Contract into the database.
   *
   * @param contract Contract to be inserted
   */
  public void insert(Contract contract) {
    ContractRoomDatabase.databaseWriteExecutor.execute(() -> contractDAO.insertContract(contract));
  }

  /**
   * Delete a Contract in the database.
   *
   * @param contract Contract to be deleted
   */
  public void delete(Contract contract) {
    ContractRoomDatabase.databaseWriteExecutor.execute(() -> contractDAO.deleteContract(contract));
  }

  /**
   * Delete a List of Contracts.
   *
   * @param contracts List of to be deleted Contracts
   */
  public void delete(List<Contract> contracts) {
    ContractRoomDatabase.databaseWriteExecutor.execute(
        () -> contractDAO.deleteContracts(contracts));
  }

  /**
   * Get a List of Contracts of which the model's name contains a give String.
   *
   * @param query String to be filtered by
   * @return Filtered List of Contracts contained in a {@link LiveData}
   */
  public LiveData<List<Contract>> getAllContractsByQuery(String query) {
    return contractDAO.getAllContractsByQueryMinSortedByDate(query);
  }
}
