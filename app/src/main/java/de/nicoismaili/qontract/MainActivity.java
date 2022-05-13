package de.nicoismaili.qontract;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import de.nicoismaili.qontract.ui.contractlist.ContractViewModel;
import de.nicoismaili.qontract.ui.fragments.EditContractFragmentDirections;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ContractViewModel viewModel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.viewModel = new ViewModelProvider(this).get(ContractViewModel.class);
        NavHostFragment hostFragment = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert hostFragment != null;
        NavController navController = hostFragment.getNavController();
        Toolbar toolbar = findViewById(R.id.main_app_bar);
        setSupportActionBar(toolbar);
        this.appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (item.getItemId() == R.id.delete_btn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Permanently delete this contract?")
                    .setNegativeButton("Cancel", (dialog, id) -> {
                    })
                    .setPositiveButton("Delete", (dialog, id) -> {
                        viewModel.deleteCurrentContract();
                        NavDirections action = EditContractFragmentDirections.actionEditContractFragmentToContractsFragment();
                        navController.navigate(action);
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (item.getItemId() == R.id.share_btn) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, Objects.requireNonNull(viewModel.getCurrentContract().getValue()).toString());
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}