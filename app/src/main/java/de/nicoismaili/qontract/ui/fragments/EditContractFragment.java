package de.nicoismaili.qontract.ui.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
import androidx.preference.PreferenceManager;

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
 */
public class EditContractFragment extends Fragment {

    private FragmentEditContractBinding binding;
    private ContractViewModel viewModel;
    private EditText dateEditText;
    private SignaturePad signaturePad;
    private NavController navController;
    private AlertDialog.Builder builder;

    public EditContractFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.edit_contract_menu, menu);
        if (EditContractFragmentArgs.fromBundle(getArguments()).getIsNewContract()) {
            menu.removeItem(R.id.share_btn);
            menu.removeItem(R.id.del_btn);
            menu.removeItem(R.id.qr_btn);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("InflateParams")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.del_btn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Delete contract")
                    .setMessage("Permanently delete this contract?")
                    .setNegativeButton("Cancel", (dialog, id) -> {
                    })
                    .setPositiveButton("Delete", (dialog, id) -> {
                        viewModel.deleteCurrentContract();
                        NavDirections action = EditContractFragmentDirections.gotoContractsFromEditAction();
                        navController.navigate(action);
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (item.getItemId() == R.id.share_btn) {
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(requireContext());
            String firstname = sharedPreferences.getString(getString(R.string.firstname_key), "");
            String lastname = sharedPreferences.getString(getString(R.string.lastname_key), "");
            String address = sharedPreferences.getString(getString(R.string.address_key), "");
            String email = sharedPreferences.getString(getString(R.string.email_key), "");
            String phone = sharedPreferences.getString(getString(R.string.phone_key), "");
            if (!firstname.isEmpty() && !lastname.isEmpty() && !address.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                Contract boundContract = binding.getContract();
                if (boundContract.isValid()) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, boundContract.toString());
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                } else {
                    Toast.makeText(requireContext(), "Please fill out all required fields before exporting!", Toast.LENGTH_LONG).show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Export Error")
                        .setMessage("Photographer details must be set before exporting a contract!")
                        .setNegativeButton("Cancel", (dialog, id) -> {
                        })
                        .setPositiveButton("Go to settings", (dialog, id) -> {
                            NavDirections action = EditContractFragmentDirections.gotoSettingsFromEditAction();
                            navController.navigate(action);
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else if (item.getItemId() == R.id.qr_btn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setView(getLayoutInflater().inflate(R.layout.dialog_qr, null))
                    .setPositiveButton("Close", (dialog, id) -> {
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.viewModel = new ViewModelProvider(requireActivity()).get(ContractViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_contract, container, false);
        binding.setContract(new Contract());
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
                    builder = new AlertDialog.Builder(requireActivity());
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
            viewModel.setCurrentContract(binding.getContract());
            NavDirections action = EditContractFragmentDirections.gotoReadFromEditAction();
            this.navController.navigate(action);
        });
        CheckBox isReadCheckbox = view.findViewById(R.id.is_read_check);
        isReadCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Contract boundContract = binding.getContract();
            boundContract.setRead(isChecked);
            if (!isChecked) {
                signaturePad.clear();
            }
            binding.setContract(boundContract);
        });
        AppCompatButton submitBtn = view.findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(v -> {
            Contract boundContract = binding.getContract();
            if (boundContract.isValid()) {
                viewModel.insert(boundContract);
                NavDirections action = EditContractFragmentDirections.gotoContractsFromEditAction();
                this.navController.navigate(action);
            } else {
                if (boundContract.hasMinFields()) {
                    builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("Submission Error")
                            .setMessage("Required fields are missing!")
                            .setNegativeButton("Close", (dialog, id) -> {
                            })
                            .setPositiveButton("Submit anyway", (dialog, id) -> {
                                viewModel.insert(boundContract);
                                NavDirections action = EditContractFragmentDirections.gotoContractsFromEditAction();
                                navController.navigate(action);
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    signaturePad.clear();
                    signaturePad.setEnabled(false);
                    builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("Submission Error")
                            .setMessage("Please fill out the minimum required fields before submitting!")
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