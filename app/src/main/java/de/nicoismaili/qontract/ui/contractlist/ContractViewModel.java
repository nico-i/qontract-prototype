package de.nicoismaili.qontract.ui.contractlist;

import static androidx.lifecycle.Transformations.switchMap;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import de.nicoismaili.qontract.data.contract.ContractRepository;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import java.util.ArrayList;
import java.util.List;

public class ContractViewModel extends AndroidViewModel {

  private final LiveData<List<Contract>> allContracts;
  private final List<Contract> selectedContracts;
  private final MutableLiveData<String> searchQuery;
  private final MutableLiveData<Contract> currentContract;
  private final ContractRepository repo;

  public ContractViewModel(Application application) {
    super(application);
    this.repo = new ContractRepository(application);
    this.searchQuery = new MutableLiveData<>();
    this.currentContract = new MutableLiveData<>();
    this.selectedContracts = new ArrayList<>();
    // Update contracts when search string changes
    this.allContracts =
        switchMap(
            this.searchQuery,
            queryString -> {
              if (queryString.equals("")) {
                return repo.getAllContracts();
              } else {
                return repo.getAllContractsByQuery(queryString);
              }
            });
  }

  /**
   * Getter for {@link #allContracts}.
   *
   * @return {@link #allContracts}
   */
  public LiveData<List<Contract>> getAllContracts() {
    return this.allContracts;
  }

  /**
   * Inserts a contract into the data base.
   *
   * @param contract The Contract to be inserted
   */
  public void insert(Contract contract) {
    repo.insert(contract);
  }

  /**
   * Sets value of {@link #searchQuery}.
   *
   * @param newQuery The new search string
   */
  public void setSearchQuery(String newQuery) {
    this.searchQuery.setValue("%" + newQuery + "%");
  }

  /**
   * Setter for {@link #currentContract}.
   *
   * @param contract The newly selected Contract
   */
  public void setCurrentContract(Contract contract) {
    this.currentContract.setValue(contract);
  }

  /**
   * Adds a Contract to {@link #selectedContracts}.
   *
   * @param contract The Contract to be added to the selection
   */
  public void addContractsToSelected(Contract contract) {
    this.selectedContracts.add(contract);
  }

  /**
   * Getter for {@link #currentContract}.
   *
   * @return {@link #currentContract}
   */
  public LiveData<Contract> getCurrentContract() {
    return this.currentContract;
  }

  /** Deletes the {@link #currentContract}. */
  public void deleteCurrentContract() {
    repo.delete(this.currentContract.getValue());
  }

  /** Deletes alls {@link #selectedContracts}. */
  public void deleteSelected() {
    repo.delete(this.selectedContracts);
  }
}
