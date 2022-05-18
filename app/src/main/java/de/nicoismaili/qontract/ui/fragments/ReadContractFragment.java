package de.nicoismaili.qontract.ui.fragments;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import de.nicoismaili.qontract.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadContractFragment extends Fragment {

    public ReadContractFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    /**
     * Initialize the contents of the Fragment host's standard options menu.
     * You should place your menu items in to menu.
     * For this method to be called, you must have first called setHasOptionsMenu.
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_read_contract, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);
        AppCompatButton backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {
            ReadContractFragmentDirections.GotoEditFromReadAction action = ReadContractFragmentDirections.gotoEditFromReadAction();
            action.setIsNewContract(false);
            navController.navigate(action);
        });
        super.onViewCreated(view, savedInstanceState);
    }
}