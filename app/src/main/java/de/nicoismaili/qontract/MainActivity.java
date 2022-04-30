package de.nicoismaili.qontract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import de.nicoismaili.qontract.data.contract.pojo.Contract;
import de.nicoismaili.qontract.ui.mainscreen.ContractListAdapter;
import de.nicoismaili.qontract.ui.mainscreen.ContractViewModel;

public class MainActivity extends AppCompatActivity {

    private ContractViewModel contractViewModel;
    private ActivityResultLauncher<Intent> editContractActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Contract contract = new Contract(-3, false, false, new Date(), "Test, Testania", "Testname", "Testlastname", "", "", "", null, null, null);
                    contractViewModel.insert(contract);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ContractListAdapter adapter = new ContractListAdapter(new ContractListAdapter.ContractDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contractViewModel = new ViewModelProvider(this).get(ContractViewModel.class);
        // Update the cached copy of the words in the adapter.
        contractViewModel.getAllContracts().observe(this, adapter::submitList);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditContractActivity.class);
            editContractActivityResultLauncher.launch(intent);
        });

    }

}