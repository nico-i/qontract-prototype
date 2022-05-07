package de.nicoismaili.qontract.ui.mainscreen;

import static androidx.lifecycle.Transformations.switchMap;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import de.nicoismaili.qontract.data.contract.ContractRepository;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import de.nicoismaili.qontract.data.contract.pojo.ContractMin;

public class ContractViewModel extends AndroidViewModel {
    private final LiveData<List<ContractMin>> allContracts;
    private final ContractRepository repo;
    private final MutableLiveData<String> searchQuery;

    public ContractViewModel(Application application) {
        super(application);
        this.repo = new ContractRepository(application);
        this.searchQuery = new MutableLiveData<>();
        this.allContracts = switchMap(this.searchQuery, queryString -> {
            if (queryString.equals("")) {
                return repo.getAllContracts();
            } else {
                return repo.getAllContractsByQuery(queryString);
            }
        });
    }

    public LiveData<List<ContractMin>> getAllContracts() {
        return this.allContracts;
    }

    public void insert(Contract contract) {
        repo.insert(contract);
    }

    public void setSearchQuery(String newQuery) {
        this.searchQuery.setValue(newQuery);
    }


}
