package de.nicoismaili.qontract.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import de.nicoismaili.qontract.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditContractFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditContractFragment extends Fragment {

    public EditContractFragment() {

    }

    public static EditContractFragment newInstance() {
        return new EditContractFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_contract, container, false);
    }
}