package de.nicoismaili.qontract.ui.contractlist;

import static androidx.lifecycle.Transformations.switchMap;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import de.nicoismaili.qontract.data.contract.ContractRepository;
import de.nicoismaili.qontract.data.contract.pojo.Contract;

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
        this.allContracts = switchMap(this.searchQuery, queryString -> {
            if (queryString.equals("")) {
                return repo.getAllContracts();
            } else {
                return repo.getAllContractsByQuery(queryString);
            }
        });
    }

    public LiveData<List<Contract>> getAllContracts() {
        return this.allContracts;
    }

    public void insert(Contract contract) {
        repo.insert(contract);
    }

    public void setSearchQuery(String newQuery) {
        this.searchQuery.setValue("%" + newQuery + "%");
    }

    public void setCurrentContract(Contract contract) {
        this.currentContract.setValue(contract);
    }

    public void addContractsToSelected(Contract contract) {
        this.selectedContracts.add(contract);
    }

    public void removeSelectedContract(Contract contract) {
        this.selectedContracts.remove(contract);
    }

    public LiveData<Contract> getCurrentContract() {
        return this.currentContract;
    }

    public void deleteCurrentContract() {
        repo.delete(this.currentContract.getValue());
    }

    public void deleteSelected() {
        repo.delete(this.selectedContracts);
    }
}
