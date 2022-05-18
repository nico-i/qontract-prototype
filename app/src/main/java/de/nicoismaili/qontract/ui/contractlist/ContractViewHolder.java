package de.nicoismaili.qontract.ui.contractlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import de.nicoismaili.qontract.R;
import de.nicoismaili.qontract.data.contract.pojo.Contract;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class ContractViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener, View.OnLongClickListener {
  private final TextView modelNameView;
  private final TextView locationView;
  private final TextView dateView;
  private final ImageView signedView;
  private final ContractListAdapter.OnContractClickListener onContractClickListener;

  private ContractViewHolder(
      View itemView, ContractListAdapter.OnContractClickListener onContractClickListener) {
    super(itemView);
    this.modelNameView = itemView.findViewById(R.id.model_name_view);
    this.locationView = itemView.findViewById(R.id.location_view);
    this.dateView = itemView.findViewById(R.id.date_view);
    this.signedView = itemView.findViewById(R.id.signed_icon);
    this.onContractClickListener = onContractClickListener;
    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(this);
  }

  static ContractViewHolder create(
      ViewGroup parent, ContractListAdapter.OnContractClickListener onContractClickListener) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contract, parent, false);
    return new ContractViewHolder(view, onContractClickListener);
  }

  public void bind(Contract contract) {
    String firstName =
        contract.getModelFirstname().substring(0, 1).toUpperCase()
            + contract.getModelFirstname().substring(1);
    String lastName =
        contract.getModelLastname().substring(0, 1).toUpperCase()
            + contract.getModelLastname().substring(1);
    String fullName = String.format("%s %s", firstName, lastName);
    String firstInitial = "";
    if (fullName.length() > 17) {
      firstInitial = firstName.charAt(0) + ". ";
      fullName = firstInitial + contract.getModelLastname();
    }
    if (fullName.length() > 17) {
      fullName = firstInitial + lastName.substring(0, 14) + "…";
    }
    modelNameView.setText(fullName);
    locationView.setText(contract.getLocation());
    Date date = new Date();
    date.setTime(contract.getDateLong());
    DateFormat dateFormat = new SimpleDateFormat(Contract.DATE_STRING_FORMAT, Locale.ROOT);
    dateView.setText(dateFormat.format(date));
    // itemView.setBackgroundColor(contract.isSelected() ? Color.GREEN : Color.WHITE);
    signedView.setBackgroundResource(
        contract.isValid() ? R.drawable.ic_signed : R.drawable.ic_unsigned);
  }

  /**
   * Called when a view has been clicked.
   *
   * @param v The view that was clicked.
   */
  @Override
  public void onClick(View v) {
    onContractClickListener.onContractClick(getAdapterPosition());
  }

  /**
   * Called when a view has been clicked and held.
   *
   * @param v The view that was clicked and held.
   * @return true if the callback consumed the long click, false otherwise.
   */
  @Override
  public boolean onLongClick(View v) {
    onContractClickListener.onContractLongClick(getAdapterPosition());
    return true;
  }
}
