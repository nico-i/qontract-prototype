package de.nicoismaili.qontract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import de.nicoismaili.qontract.data.contract.pojo.Contract;
import de.nicoismaili.qontract.ui.mainView.ContractListAdapter;
import de.nicoismaili.qontract.ui.mainView.ContractViewModel;

public class MainActivity extends AppCompatActivity implements ContractListAdapter.OnContractClickListener {

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
        // Add and initialise recyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ContractListAdapter adapter = new ContractListAdapter(new ContractListAdapter.ContractDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contractViewModel = new ViewModelProvider(this).get(ContractViewModel.class);
        contractViewModel.getAllContracts().observe(this, adapter::submitList);
        // Initialize search query
        contractViewModel.setSearchQuery("");
        // Update contracts depending on search
        SearchView searchView = findViewById(R.id.search);
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
        // Add function to add contract button
        ImageView addButton = findViewById(R.id.addContractIcon);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditContractActivity.class);
            editContractActivityResultLauncher.launch(intent);
        });
        // Add function to settings button
        ImageView settingsButton = findViewById(R.id.settingsIcon);
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            MainActivity.this.startActivity(intent);
        });
        // Change font of SearchView
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(id);
        Typeface tf = ResourcesCompat.getFont(this, R.font.poppins_medium);
        textView.setGravity(Gravity.BOTTOM);
        textView.setTypeface(tf);
        // Hide Actionbar
        Toolbar selectionToolbar = findViewById(R.id.select_contracts_toolbar);
        selectionToolbar.setTranslationY(R.dimen.xl_dim);
    }

    @Override
    public void onContractClick(int position) {
        Intent intent = new Intent(this, EditContractActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onContractLongClick(int position) {
        Vibrator vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50); // 50 is time in ms
    }
}