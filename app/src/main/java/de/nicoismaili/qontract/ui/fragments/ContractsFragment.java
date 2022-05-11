package de.nicoismaili.qontract.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.nicoismaili.qontract.R;
import de.nicoismaili.qontract.ui.contractlist.ContractListAdapter;
import de.nicoismaili.qontract.ui.contractlist.ContractViewModel;

public class ContractsFragment extends Fragment {
    private ContractViewModel contractViewModel;
    private ContractListAdapter.OnContractClickListener onContractClickListener;

    public ContractsFragment() {
        super(R.layout.fragment_contracts);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.onContractClickListener = (ContractListAdapter.OnContractClickListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context + " must implement OnCompleteListener");
        }
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.contractViewModel = new ViewModelProvider(this).get(ContractViewModel.class);
        Toolbar toolbar = view.findViewById(R.id.main_app_bar);
        NavController navController = Navigation.findNavController(view);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_contract) {
                NavDirections action = ContractsFragmentDirections.actionContractsFragmentToEditContractFragment();
                navController.navigate(action);
            }
            return true;
        });
        toolbar.setNavigationOnClickListener(v -> {
            NavDirections action = ContractsFragmentDirections.actionContractsFragmentToSettingsFragment();
            navController.navigate(action);
        });
        // Add and initialise recyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final ContractListAdapter adapter = new ContractListAdapter(new ContractListAdapter.ContractDiff(), this.onContractClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        contractViewModel = new ViewModelProvider(this).get(ContractViewModel.class);
        contractViewModel.getAllContracts().observe(getViewLifecycleOwner(), adapter::submitList);
        // Initialize search query
        contractViewModel.setSearchQuery("");
        // Update contracts depending on search
        SearchView searchView = view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contractViewModel.setSearchQuery(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contractViewModel.setSearchQuery(newText);
                return true;
            }
        });
    }
}
