package de.nicoismaili.qontract;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import de.nicoismaili.qontract.ui.contractlist.ContractListAdapter;
import de.nicoismaili.qontract.ui.contractlist.ContractViewModel;
import de.nicoismaili.qontract.ui.fragments.ContractsFragmentDirections;

public class MainActivity extends AppCompatActivity implements ContractListAdapter.OnContractClickListener {

    private ContractViewModel contractViewModel;
    private ActionMode actionMode;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.contractViewModel = new ViewModelProvider(this).get(ContractViewModel.class);
    }

    @Override
    public void onContractClick(int position) {
        if (actionMode == null) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavDirections action = ContractsFragmentDirections.actionContractsFragmentToEditContractFragment();
            navController.navigate(action);
        } else {
            // Toast.makeText(this, contractViewModel.getContractByPos(position).toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onContractLongClick(int position) {
        if (this.actionMode != null) {
            return;
        }
        // Provide haptic feedback to notify long click was heard
        Vibrator vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);

        this.actionMode = this.startActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.select_menu, menu);
                mode.setTitle(getResources().getString(R.string.select_contracts));
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_selected:
                        Toast.makeText(MainActivity.this, "Share pressed", Toast.LENGTH_SHORT).show();
                        mode.finish();
                        return true;
                    case R.id.delete_selected:
                        Toast.makeText(MainActivity.this, "Delete pressed", Toast.LENGTH_SHORT).show();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
            }
        });
    }
}