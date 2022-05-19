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
   * Initialize the contents of the Fragment host's standard options menu. Removes {@link MenuItem}s
   * if contract is a new contract or model mode is active.
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

  /**
   * Called to have the fragment instantiate its user interface view. This will be called between
   * {@link #onCreate(Bundle)} and {@link #onViewCreated(View, Bundle)}. Also initiates data binding
   * with the {@link DataBindingUtil}.
   *
   * @param inflater The LayoutInflater object that can be used to inflate any views in the
   *     fragment.
   * @param container If non-null, this is the parent view that the fragment's UI should be attached
   *     to. The fragment should not add the view itself, but this can be used to generate the
   *     LayoutParams of the view.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
   *     saved state as given here.
   * @return Return the View for the fragment's UI, or null.
   */
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

  /**
   * Handles the onClick Event of the QR-code {@link MenuItem} by first checking if the photographer
   * details are present in the {@link SharedPreferences}. If they are present it will call upon the
   * QR-code API and get a Bitmap for the current contract via {@link Glide}, otherwise it will
   * display an error dialog prompting the user to fill in the photographer details.
   */
  @SuppressLint("InflateParams")
  private void showQRCodeDialog() {
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
      View view = View.inflate(requireActivity(), R.layout.dialog_qr, null);
      ImageView qrImageView = view.findViewById(R.id.qr_code_image_view);
      view.setBackgroundColor(Color.WHITE);
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
                      + "- %s -\n\n"
                      + "%s %s\n"
                      + "%s\n"
                      + "%s\n"
                      + "%s",
                  R.string.photographer,
                  binding.getContract(),
                  firstname,
                  lastname,
                  address,
                  email,
                  phone))
          .placeholder(circularProgressDrawable)
          .into(qrImageView);
      AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
      builder.setPositiveButton(R.string.close, (dialog, id) -> {});
      AlertDialog alert = builder.create();
      alert.setView(view);
      alert.show();
    } else {
      showPhotographerDetailsErrorDialog();
    }
  }

  /**
   * Handles the onClick event of the delete {@link MenuItem}. It displays a confirmation dialog and
   * deletes the current contract if said dialog has a positive response.
   */
  private void showDeleteContractDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    builder
        .setTitle(R.string.del_contract_title)
        .setMessage(R.string.del_contract_prompt)
        .setNegativeButton(android.R.string.cancel, (dialog, id) -> {})
        .setPositiveButton(
            R.string.delete,
            (dialog, id) -> {
              this.viewModel.deleteCurrentContract();
              NavDirections action = EditContractFragmentDirections.gotoContractsFromEditAction();
              this.navController.navigate(action);
            });
    AlertDialog alert = builder.create();
    alert.show();
  }

  /**
   * Shows a dialog prompting the user to add the photographer's details in the settings if they
   * haven't been set. Also navigates to the {@link SettingsFragment} if the user follows the
   * suggestion.
   */
  private void showPhotographerDetailsErrorDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
    builder
        .setTitle(R.string.export_error_message)
        .setMessage(R.string.photographer_details_error)
        .setNegativeButton(android.R.string.cancel, (dialog, id) -> {})
        .setPositiveButton(
            R.string.go_to_settings_btn,
            (dialog, id) -> {
              NavDirections action = EditContractFragmentDirections.gotoSettingsFromEditAction();
              this.navController.navigate(action);
            });
    AlertDialog alert = builder.create();
    alert.show();
  }

  /**
   * Handles the share {@link MenuItem} click event. Checks if photographer details have been set
   * and calls {@link #showPhotographerDetailsErrorDialog()} otherwise. If they have been set it
   * calls {@link Contract#isValid()} on the current contract to check its validity. If it is valid
   * it creates a new {@link Intent} with a send action to open the Android share menu, otherwise it
   * will display a toast stating that some fields are missing.
   */
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
                "%s\n\n- %s -\n\n%s %s\n%s\n%s\n%s",
                boundContract, R.string.photographer, firstname, lastname, address, email, phone));
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
      } else {
        Toast.makeText(requireContext(), R.string.req_fields_error, Toast.LENGTH_LONG).show();
      }
    } else {
      showPhotographerDetailsErrorDialog();
    }
  }

  /**
   * Creates and initializes a new {@link
   * com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener} object. If the user tries
   * to sign the contract without having read it, it will display a dialog informing the user that
   * the contract needs to be read before signing and disable the signature pad. The {@link
   * Contract}'s signature bitmap is then set and unset depending on there being a signature.
   *
   * @return The configured {@link
   *     com.github.gcacace.signaturepad.views.SignaturePad.OnSignedListener} object
   */
  private SignaturePad.OnSignedListener onSignaturePadSigned() {
    return new SignaturePad.OnSignedListener() {
      @Override
      public void onStartSigning() {
        if (!binding.getContract().isRead()) {
          signaturePad.clear();
          signaturePad.setEnabled(false);
          AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
          builder
              .setMessage(R.string.prompt_read_before_signing)
              .setCancelable(false)
              .setPositiveButton(
                  android.R.string.ok,
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

  /**
   * Is called as soon as the currentContract in {@link #viewModel} is updated. Resets {@link
   * #binding} to new contract and updates necessary views.
   *
   * @param newContract The newly set contract
   */
  private void onCurrentContractChanged(Contract newContract) {
    this.binding.setContract(new Contract(newContract));
    SimpleDateFormat sdf = new SimpleDateFormat(Contract.DATE_STRING_FORMAT, Locale.ROOT);
    this.dateField.setText(sdf.format(new Date(newContract.getDateLong())));
    this.isReadCheckbox.setChecked(newContract.isRead());
    if (newContract.getModelSignature() != null) {
      this.signaturePad.setSignatureBitmap(newContract.getModelSignature());
    }
  }

  /**
   * Handles the onClick event on the submit button. Saves the contract to the database if it is
   * valid. If model mode is on, submission is restricted without having filled out all required
   * fields beforehand, this is shown via a {@link Toast}. If model mode is off and there are
   * missing <b>minimum</b> required fields, it shows a toast that tells the user to fill out said
   * fields before continuing. If only required fields are missing, the user is shown a dialog in
   * which he can "submit anyways" or close the dialog to continue editing the {@link Contract}.
   *
   * @param v The clicked submit button's view
   */
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
              .setTitle(R.string.submit_error_title)
              .setMessage(R.string.req_fields_error)
              .setNegativeButton(R.string.close, (dialog, id) -> {})
              .setPositiveButton(
                  R.string.submit_anyway,
                  (dialog, id) -> {
                    viewModel.insert(boundContract);
                    NavDirections action =
                        EditContractFragmentDirections.gotoContractsFromEditAction();
                    navController.navigate(action);
                  });
          AlertDialog alert = builder.create();
          alert.show();
        } else {
          Toast.makeText(requireContext(), R.string.toast_min_fields_error, Toast.LENGTH_LONG)
              .show();
        }
      } else {
        Toast.makeText(requireContext(), R.string.req_fields_error, Toast.LENGTH_LONG).show();
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
