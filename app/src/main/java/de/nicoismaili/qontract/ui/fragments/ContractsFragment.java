package de.nicoismaili.qontract.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import de.nicoismaili.qontract.R;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import de.nicoismaili.qontract.ui.contractlist.ContractListAdapter;
import de.nicoismaili.qontract.ui.contractlist.ContractViewModel;
import java.util.Objects;

/** A simple {@link Fragment} subclass containing the Contracts overview. */
public class ContractsFragment extends Fragment
    implements ContractListAdapter.OnContractClickListener {

  private ContractViewModel viewModel;
  private NavController navController;
  private ActionMode actionMode;

  public ContractsFragment() {
    super(R.layout.fragment_contracts);
  }

  /**
   * Called when a fragment is first attached to its context. {@link #onCreate(Bundle)} will be
   * called after this.
   *
   * @param context The current {@link Context}
   */
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
  }

  /**
   * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}. Initializes
   * {@link #navController} and {@link #viewModel}, creates Contract RecyclerView and sets Listeners
   * on certain views.
   *
   * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
   *     saved state as given here.
   */
  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.navController = Navigation.findNavController(view);
    this.viewModel = new ViewModelProvider(requireActivity()).get(ContractViewModel.class);
    // Add and initialise recyclerView
    RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
    final ContractListAdapter adapter =
        new ContractListAdapter(new ContractListAdapter.ContractDiff(), this);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    this.viewModel = new ViewModelProvider(requireActivity()).get(ContractViewModel.class);
    this.viewModel.getAllContracts().observe(getViewLifecycleOwner(), adapter::submitList);
    // Initialize search query
    this.viewModel.setSearchQuery("");
    // Update contracts depending on search
    SearchView searchView = view.findViewById(R.id.search);
    searchView.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
            viewModel.setSearchQuery(query);
            searchView.clearFocus();
            return true;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
            viewModel.setSearchQuery(newText);
            return true;
          }
        });
    FloatingActionButton fab = view.findViewById(R.id.fab);
    fab.setOnClickListener(this::onAddContractClick);
  }

  /**
   * If {@link #actionMode} is active, add the contract to the selection, otherwise navigates to
   * {@link EditContractFragment} with isNewContract to false.
   *
   * @param position Position of the clicked item in the RecyclerView
   */
  @Override
  public void onContractClick(int position) {
    Contract clickedContract =
        Objects.requireNonNull(this.viewModel.getAllContracts().getValue()).get(position);
    if (actionMode == null) {
      this.viewModel.setCurrentContract(clickedContract);
      ContractsFragmentDirections.EditContractAction action =
          ContractsFragmentDirections.editContractAction();
      action.setIsNewContract(false);
      this.navController.navigate(action);
    } else {
      viewModel.addContractsToSelected(clickedContract);
    }
  }

  /**
   * Activates {@link #actionMode} if it's inactive or does nothing otherwise.
   *
   * @param position Position of the clicked item in the RecyclerView
   */
  @Override
  public void onContractLongClick(int position) {
    if (this.actionMode != null) {
      return;
    }
    // Provide haptic feedback to notify long click was noticed
    Vibrator vibe = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
    vibe.vibrate(50);
    // Initializes actionMode
    this.actionMode =
        requireActivity()
            .startActionMode(
                new ActionMode.Callback() {
                  @Override
                  public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.select_menu, menu);
                    mode.setTitle(
                        getResources().getString(R.string.title_actionbar_select_contracts));
                    return true;
                  }

                  @Override
                  public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                  }

                  @Override
                  public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    if (item.getItemId() == R.id.share_selected) {
                      Intent sendIntent = new Intent();
                      sendIntent.setAction(Intent.ACTION_SEND);
                      sendIntent.putExtra(Intent.EXTRA_TEXT, "Placeholder");
                      sendIntent.setType("text/plain");
                      Intent shareIntent = Intent.createChooser(sendIntent, null);
                      startActivity(shareIntent);
                      mode.finish();
                      return true;
                    }
                    if (item.getItemId() == R.id.delete_selected) {
                      viewModel.deleteSelected();
                      mode.finish();
                      return true;
                    }
                    return false;
                  }

                  @Override
                  public void onDestroyActionMode(ActionMode mode) {
                    actionMode = null;
                  }
                });
    viewModel.addContractsToSelected(
        Objects.requireNonNull(this.viewModel.getAllContracts().getValue()).get(position));
  }

  /**
   * Navigates to {@link EditContractFragment} while setting the isNewContract to true.
   *
   * @param v The view of the clicked button.
   */
  private void onAddContractClick(View v) {
    this.viewModel.setCurrentContract(new Contract());
    ContractsFragmentDirections.EditContractAction action =
        ContractsFragmentDirections.editContractAction();
    action.setIsNewContract(true);
    this.navController.navigate(action);
  }
}
