package de.nicoismaili.qontract;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
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
        SearchView searchView = findViewById(R.id.search);
        contractViewModel.setSearchQuery("");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                contractViewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contractViewModel.setSearchQuery(newText);
                return true;
            }
        });
        ImageView addButton = findViewById(R.id.addContractIcon);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditContractActivity.class);
            editContractActivityResultLauncher.launch(intent);
        });
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        Typeface tf = ResourcesCompat.getFont(this, R.font.poppins_medium);
        textView.setGravity(Gravity.BOTTOM);
        textView.setTypeface(tf);
    }

}