package de.nicoismaili.qontract.ui.mainscreen;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.nicoismaili.qontract.data.contract.ContractRepository;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import de.nicoismaili.qontract.data.contract.pojo.ContractMin;

public class ContractViewModel extends AndroidViewModel {
    private final LiveData<List<ContractMin>> allContracts;
    private ContractRepository repo;

    public ContractViewModel(Application application) {
        super(application);
        repo = new ContractRepository(application);
        allContracts = repo.getAllContracts();
    }

    public LiveData<List<ContractMin>> getAllContracts() {
        return allContracts;
    }

    public void insert(Contract contract) {
        repo.insert(contract);
    }

}
