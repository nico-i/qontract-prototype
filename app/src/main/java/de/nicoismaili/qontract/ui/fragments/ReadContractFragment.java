package de.nicoismaili.qontract.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import de.nicoismaili.qontract.R;

/**
 * A simple {@link Fragment} subclass containing the the contract text.
 */
public class ReadContractFragment extends Fragment {

    public ReadContractFragment() {
        // Required empty public constructor
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu. You should place your
     * menu items in to menu. For this method to be called, you must have first called
     * setHasOptionsMenu.
     *
     * @param menu     – The options menu in which you place your items.
     * @param inflater – The inflater which will inflate the menu.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.read_contract_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Called to have the fragment instantiate / inflate its user interface view.
     * This method is called between {@link #onCreate(Bundle)} and {@link #onViewCreated(View, Bundle)}.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read_contract, container, false);
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view. Initializes
     * {@link android.view.View.OnClickListener} for the "back to contract" button.
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppCompatButton backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(
                v -> {
                    NavController navController = Navigation.findNavController(view);
                    ReadContractFragmentDirections.GotoEditFromReadAction action =
                            ReadContractFragmentDirections.gotoEditFromReadAction();
                    action.setIsNewContract(false);
                    navController.navigate(action);
                });
        super.onViewCreated(view, savedInstanceState);
    }
}
