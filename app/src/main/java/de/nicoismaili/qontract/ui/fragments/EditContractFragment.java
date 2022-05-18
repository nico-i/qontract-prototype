package de.nicoismaili.qontract.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.github.gcacace.signaturepad.views.SignaturePad;
import de.nicoismaili.qontract.R;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import de.nicoismaili.qontract.databinding.FragmentEditContractBinding;
import de.nicoismaili.qontract.ui.contractlist.ContractViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** A {@link Fragment} subclass containing the view for editing (and creating) a contract. */
public class EditContractFragment extends Fragment {

  private FragmentEditContractBinding binding;
  private ContractViewModel viewModel;
  private EditText dateField;
  private SignaturePad signaturePad;
  private NavController navController;
  private CheckBox isReadCheckbox;

  public EditContractFragment() {
    // Required empty public constructor
  }

  /**
   * Initialize the contents of the Fragment host's standard options menu. Removes menu items if
   * contract is a new contract or model mode is active.
   *
   * @param menu The options menu in which you place your items.
   * @param inflater Inflates the menu from an XML.
   */
  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    menu.clear();
    inflater.inflate(R.menu.edit_contract_menu, menu);
    SharedPreferences sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(requireContext());
    if (EditContractFragmentArgs.fromBundle(getArguments()).getIsNewContract()
        || sharedPreferences.getBoolean(getString(R.string.model_mode_key), false)) {
      menu.removeItem(R.id.share_btn);
      menu.removeItem(R.id.del_btn);
      menu.removeItem(R.id.qr_btn);
    }
    super.onCreateOptionsMenu(menu, inflater);
  }

  /**
   * This hook is called whenever an item in the options menu is pressed and handles the
   * onClickEvent accordingly.
   *
   * @param item The menu item that was selected.
   * @return boolean Return false to allow normal menu processing to proceed, true to consume it
   *     here.
   * @see #showDeleteContractDialog()
   * @see #handleShareButtonClick()
   * @see #showQRCodeDialog()
   */
  @SuppressLint("InflateParams")
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.del_btn) {
      showDeleteContractDialog();
    } else if (item.getItemId() == R.id.share_btn) {
      handleShareButtonClick();
    } else if (item.getItemId() == R.id.qr_btn) {
      showQRCodeDialog();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Called to do initial creation of a fragment. This is called after {@link #onAttach(Activity)}
   * and before {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}. Initializes ViewModel.
   *
   * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
   *     is the state.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setHasOptionsMenu(true);
    this.viewModel = new ViewModelProvider(requireActivity()).get(ContractViewModel.class);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.binding =
        DataBindingUtil.inflate(inflater, R.layout.fragment_edit_contract, container, false);
    this.binding.setContract(new Contract());
    return this.binding.getRoot();
  }

  /**
   * Sets all handlers for the views that do not have 2-way data binding, handles a change on the
   * currently selected contract and initializes the {@link NavController}.
   *
   * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
   *     saved state as given here.
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Get NavController
    this.navController = Navigation.findNavController(view);
    // Handle dateField clicks
    this.dateField = view.findViewById(R.id.date_input);
    this.dateField.setOnClickListener(this::onDateFieldClick);
    // Handle signing on signaturePad
    this.signaturePad = view.findViewById(R.id.signature_pad);
    this.signaturePad.setOnSignedListener(onSignaturePadSigned());
    // Handle "Read contract" Button clicks
    AppCompatButton readBtn = view.findViewById(R.id.read_contract_btn);
    readBtn.setOnClickListener(this::onReadBtnClick);
    // Handle isRead checkbox clicks
    this.isReadCheckbox = view.findViewById(R.id.is_read_check);
    this.isReadCheckbox.setOnCheckedChangeListener(this::onIsReadCheckedChanged);
    // Handle contract submission
    AppCompatButton submitBtn = view.findViewById(R.id.submit_btn);
    submitBtn.setOnClickListener(this::onSubmitButtonClick);
    // Handle current contract change
    viewModel.getCurrentContract().observe(getViewLifecycleOwner(), this::onCurrentContractChanged);
  }

  /**
   * Opens a new {@link DatePickerDialog} when the date EditText is pressed and sets the date
   * field's EditText text to the resulting date.
   *
   * @param v The view that was clicked.
   */
  private void onDateFieldClick(View v) {
    Calendar calendar = Calendar.getInstance();
    new DatePickerDialog(
            EditContractFragment.this.getContext(),
            (view1, year, month, dayOfMonth) -> {
              calendar.set(Calendar.YEAR, year);
              calendar.set(Calendar.MONTH, month);
              calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
              Date calendarDate = calendar.getTime();
              Contract boundContract = binding.getContract();
              boundContract.setDateLong(calendarDate.getTime());
              binding.setContract(boundContract);
              SimpleDateFormat simpleDateFormat =
                  new SimpleDateFormat(Contract.DATE_STRING_FORMAT, Locale.ROOT);
              dateField.setText(simpleDateFormat.format(calendarDate));
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        .show();
  }

  @SuppressLint("InflateParams")
  private void showQRCodeDialog() {
    View view = View.inflate(requireActivity(), R.layout.dialog_qr, null);
    ImageView qrImageView = (ImageView) view.findViewById(R.id.qr_code_image_view);
    view.setBackgroundColor(Color.WHITE);
    SharedPreferences sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(requireContext());
    String firstname = sharedPreferences.getString(getString(R.string.firstname_key), "");
    String lastname = sharedPreferences.getString(getString(R.string.lastname_key), "");
    String address = sharedPreferences.getString(getString(R.string.address_key), "");
    String email = sharedPreferences.getString(getString(R.string.email_key), "");
    String phone = sharedPreferences.getString(getString(R.string.phone_key), "");
    CircularProgressDrawable circularProgressDrawable =
        new CircularProgressDrawable(requireContext());
    circularProgressDrawable.setStrokeWidth(this.getResources().getDimension(R.dimen.s_dim));
    circularProgressDrawable.setCenterRadius(this.getResources().getDimension(R.dimen.l_dim));
    circularProgressDrawable.setTint(this.getResources().getColor(R.color.blue_500));
    circularProgressDrawable.start();
    Glide.with(this)
        .load(
            String.format(
                "https://api.qrserver.com/v1/create-qr-code/?size=500x500&data=%s\n\n"
                    + "- Photographer -\n\n"
                    + "%s %s\n"
                    + "%s\n"
                    + "%s\n"
                    + "%s",
                binding.getContract(), firstname, lastname, address, email, phone))
        .placeholder(circularProgressDrawable)
        .into(qrImageView);
    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
    builder.setPositiveButton("Close", (dialog, id) -> {});
    AlertDialog alert = builder.create();
    alert.setView(view);
    alert.show();
  }

  private void showDeleteContractDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    builder
        .setTitle("Delete contract")
        .setMessage("Permanently delete this contract?")
        .setNegativeButton("Cancel", (dialog, id) -> {})
        .setPositiveButton(
            "Delete",
            (dialog, id) -> {
              this.viewModel.deleteCurrentContract();
              NavDirections action = EditContractFragmentDirections.gotoContractsFromEditAction();
              this.navController.navigate(action);
            });
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void showPhotographerDetailsExportErrorDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    builder
        .setTitle("Export Error")
        .setMessage("Photographer details must be set before exporting a contract!")
        .setNegativeButton("Cancel", (dialog, id) -> {})
        .setPositiveButton(
            "Go to settings",
            (dialog, id) -> {
              NavDirections action = EditContractFragmentDirections.gotoSettingsFromEditAction();
              this.navController.navigate(action);
            });
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void handleShareButtonClick() {
    SharedPreferences sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(requireContext());
    String firstname = sharedPreferences.getString(getString(R.string.firstname_key), "");
    String lastname = sharedPreferences.getString(getString(R.string.lastname_key), "");
    String address = sharedPreferences.getString(getString(R.string.address_key), "");
    String email = sharedPreferences.getString(getString(R.string.email_key), "");
    String phone = sharedPreferences.getString(getString(R.string.phone_key), "");
    if (!firstname.isEmpty()
        && !lastname.isEmpty()
        && !address.isEmpty()
        && !email.isEmpty()
        && !phone.isEmpty()) {
      Contract boundContract = this.binding.getContract();
      if (boundContract.isValid()) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            String.format(
                "%s\n\n- Photographer -\n\n%s %s\n%s\n%s\n%s",
                boundContract, firstname, lastname, address, email, phone));
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
      } else {
        Toast.makeText(
                requireContext(),
                "Please fill out all required fields before exporting!",
                Toast.LENGTH_LONG)
            .show();
      }
    } else {
      showPhotographerDetailsExportErrorDialog();
    }
  }

  private SignaturePad.OnSignedListener onSignaturePadSigned() {
    return new SignaturePad.OnSignedListener() {
      @Override
      public void onStartSigning() {
        if (!binding.getContract().isRead()) {
          signaturePad.clear();
          signaturePad.setEnabled(false);
          AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
          builder
              .setMessage("Please read the contract before signing!")
              .setCancelable(false)
              .setPositiveButton(
                  "OK",
                  (dialog, id) -> {
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
    };
  }

  private void onCurrentContractChanged(Contract newContract) {
    this.binding.setContract(new Contract(newContract));
    SimpleDateFormat sdf = new SimpleDateFormat(Contract.DATE_STRING_FORMAT, Locale.ROOT);
    this.dateField.setText(sdf.format(new Date(newContract.getDateLong())));
    this.isReadCheckbox.setChecked(newContract.isRead());
    if (newContract.getModelSignature() != null) {
      this.signaturePad.setSignatureBitmap(newContract.getModelSignature());
    }
  }

  private void onSubmitButtonClick(View v) {
    Contract boundContract = binding.getContract();
    if (boundContract.isValid()) {
      viewModel.insert(boundContract);
      NavDirections action = EditContractFragmentDirections.gotoContractsFromEditAction();
      this.navController.navigate(action);
    } else {
      SharedPreferences sharedPreferences =
          PreferenceManager.getDefaultSharedPreferences(requireContext());
      if (!sharedPreferences.getBoolean(getString(R.string.model_mode_key), false)) {
        if (boundContract.hasMinFields()) {
          AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
          builder
              .setTitle("Submission Error")
              .setMessage("Required fields are missing!")
              .setNegativeButton("Close", (dialog, id) -> {})
              .setPositiveButton(
                  "Submit anyway",
                  (dialog, id) -> {
                    viewModel.insert(boundContract);
                    NavDirections action =
                        EditContractFragmentDirections.gotoContractsFromEditAction();
                    navController.navigate(action);
                  });
          AlertDialog alert = builder.create();
          alert.show();
        } else {
          AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
          builder
              .setTitle("Submission Error")
              .setMessage("Please fill out the minimum required fields before submitting!")
              .setCancelable(false)
              .setPositiveButton(
                  "OK",
                  (dialog, id) -> {
                    signaturePad.clear();
                    signaturePad.setEnabled(true);
                    signaturePad.clearFocus();
                  });
          AlertDialog alert = builder.create();
          alert.show();
        }
      } else {
        Toast.makeText(
                requireContext(),
                "Please fill out all required fields before submitting!",
                Toast.LENGTH_LONG)
            .show();
      }
    }
  }

  /**
   * Sets the isRead field in the bound contract to the isChecked value. Also clears the signature
   * pad if the checkbox is unchecked.
   *
   * @param buttonView The view of the click checkbox.
   * @param isChecked The state of the checkbox.
   */
  private void onIsReadCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    Contract boundContract = binding.getContract();
    boundContract.setRead(isChecked);
    if (!isChecked) {
      signaturePad.clear();
    }
    binding.setContract(boundContract);
  }

  /**
   * Navigates to the {@link ReadContractFragment} when the "Read contract" button is pressed.
   *
   * @param v The view of the clicked button.
   */
  private void onReadBtnClick(View v) {
    viewModel.setCurrentContract(binding.getContract());
    NavDirections action = EditContractFragmentDirections.gotoReadFromEditAction();
    this.navController.navigate(action);
  }
}
