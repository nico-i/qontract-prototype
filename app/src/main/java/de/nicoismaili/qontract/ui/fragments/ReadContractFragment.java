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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import de.nicoismaili.qontract.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadContractFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadContractFragment extends Fragment {
    public ReadContractFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.read_contract_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReadContractFragment.
     */
    public static ReadContractFragment newInstance() {
        ReadContractFragment fragment = new ReadContractFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
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
            NavDirections action = ReadContractFragmentDirections.gotoEditContractFromReadContract();
            navController.navigate(action);
        });
        super.onViewCreated(view, savedInstanceState);
    }
}