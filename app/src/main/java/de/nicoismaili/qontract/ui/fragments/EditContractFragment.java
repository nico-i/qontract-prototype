package de.nicoismaili.qontract.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

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
    private NavController navController;

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
        this.navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
        this.dateEditText = view.findViewById(R.id.date_input);
        Calendar calendar = Calendar.getInstance();
        dateEditText.setOnClickListener(v -> new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date calendarDate = calendar.getTime();
            Contract boundContract = binding.getContract();
            boundContract.setDate(calendarDate.getTime());
            binding.setContract(boundContract);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
            dateEditText.setText(simpleDateFormat.format(calendarDate));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
        this.signaturePad = view.findViewById(R.id.signature_pad);
        this.signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                if (!binding.getContract().isRead()) {
                    signaturePad.clear();
                    signaturePad.setEnabled(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setMessage("Please read the contract before signing!")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> {
                                signaturePad.clear();
                                signaturePad.setEnabled(true);
                                signaturePad.clearFocus();
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onSigned() {
                Contract boundContract = binding.getContract();
                boundContract.setModelSignature(signaturePad.getTransparentSignatureBitmap());
                binding.setContract(boundContract);
            }

            @Override
            public void onClear() {
                Contract boundContract = binding.getContract();
                boundContract.setModelSignature(null);
                binding.setContract(boundContract);
            }
        });
        AppCompatButton readBtn = view.findViewById(R.id.read_contract_btn);
        readBtn.setOnClickListener(v -> {
            NavDirections action = EditContractFragmentDirections.actionEditContractFragmentToReadContractFragment();
            this.navController.navigate(action);
        });
        AppCompatButton submitBtn = view.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(v -> {
            Contract boundContract = binding.getContract();
            if (boundContract.isValid()) {
                viewModel.insert(boundContract);
                NavDirections action = EditContractFragmentDirections.actionEditContractFragmentToContractsFragment();
                this.navController.navigate(action);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("Required fields are missing!")
                        .setNegativeButton("Cancel", (dialog, id) -> {
                        })
                        .setPositiveButton("Submit anyway", (dialog, id) -> {
                            viewModel.insert(boundContract);
                            NavDirections action = EditContractFragmentDirections.actionEditContractFragmentToContractsFragment();
                            navController.navigate(action);
                        });
                AlertDialog alert = builder.create();
                alert.show();
                // Toast.makeText(requireActivity(), "Please fill out all required fields before submitting.", Toast.LENGTH_LONG).show();
            }
        });
        viewModel.getCurrentContract().observe(getViewLifecycleOwner(), newContract -> {
            this.binding.setContract(new Contract(newContract));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
            dateEditText.setText(sdf.format(new Date(newContract.getDate())));
            if (newContract.getModelSignature() != null) {
                signaturePad.setSignatureBitmap(newContract.getModelSignature());
            }
        });
    }
}