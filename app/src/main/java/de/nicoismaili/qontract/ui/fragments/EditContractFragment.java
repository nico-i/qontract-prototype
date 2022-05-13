package de.nicoismaili.qontract.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.nicoismaili.qontract.R;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import de.nicoismaili.qontract.databinding.FragmentEditContractBinding;
import de.nicoismaili.qontract.ui.contractlist.ContractViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditContractFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditContractFragment extends Fragment {

    private Contract contract;
    private FragmentEditContractBinding binding;
    private ContractViewModel viewModel;
    private EditText dateEditText;
    private SignaturePad signaturePad;
    private AppCompatButton readContractBtn;
    private AppCompatButton submitBtn;

    public EditContractFragment() {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_contract_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public static EditContractFragment newInstance() {
        return new EditContractFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.viewModel = new ViewModelProvider(requireActivity()).get(ContractViewModel.class);
        this.contract = new Contract();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_contract, container, false);
        binding.setContract(contract);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getCurrentContract().observe(getViewLifecycleOwner(), newContract -> {
            if (newContract != null) {
                Log.d("onViewCreated: ", newContract.toString());
                this.binding.setContract(newContract);
            }
        });
        this.dateEditText = view.findViewById(R.id.date_input);
        Calendar calendar = Calendar.getInstance();
        dateEditText.setOnClickListener(v -> new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date calendarDate = calendar.getTime();
            contract.setDate(calendarDate.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ROOT);
            dateEditText.setText(simpleDateFormat.format(calendarDate));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
        this.signaturePad = view.findViewById(R.id.signature_pad);
        this.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                if (!contract.isSigned()) {
                    Toast.makeText(getContext(), "Please read the contract before signing", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSigned() {
                contract.setSigned(true);
                contract.setModelSignatureSVG(signaturePad.getSignatureSvg());
            }

            @Override
            public void onClear() {

            }
        });
    }
}