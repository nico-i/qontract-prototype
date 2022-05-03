package de.nicoismaili.qontract;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import de.nicoismaili.qontract.data.contract.pojo.Contract;
import de.nicoismaili.qontract.ui.mainscreen.ContractListAdapter;
import de.nicoismaili.qontract.ui.mainscreen.ContractViewModel;

public class MainActivity extends AppCompatActivity {

    private ContractViewModel contractViewModel;
    private final ActivityResultLauncher<Intent> editContractActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    assert data != null;
                    String firstname = data.getStringExtra(EditContractActivity.FIRSTNAME);
                    String lastname = data.getStringExtra(EditContractActivity.LASTNAME);
                    Contract contract = new Contract();
                    contract.setSigned(false);
                    contract.setModelFirstname(firstname);
                    contract.setModelLastname(lastname);
                    contract.setLocation("Blub");
                    contract.setDate(new Date());
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
        contractViewModel.getAllContracts().observe(this, adapter::submitList);
        ImageView fab = findViewById(R.id.addContractIcon);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditContractActivity.class);
            editContractActivityResultLauncher.launch(intent);
        });

    }

}