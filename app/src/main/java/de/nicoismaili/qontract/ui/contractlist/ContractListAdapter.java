package de.nicoismaili.qontract.ui.contractlist;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil.ItemCallback;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.nicoismaili.qontract.data.contract.pojo.Contract;

/**
 * A {@link ListAdapter} subclass for presenting contracts in a RecyclerView, including computing
 * diffs between contract lists on a background thread.
 */
public class ContractListAdapter extends ListAdapter<Contract, ContractViewHolder> {

  private final ContractListAdapter.OnContractClickListener onContractClickListener;

  public ContractListAdapter(
      @NonNull ItemCallback<Contract> diffCallback,
      OnContractClickListener onContractClickListener) {
    super(diffCallback);
    // Set initialized interface to local variable
    this.onContractClickListener = onContractClickListener;
  }

  /**
   * Called when RecyclerView needs a new {@link RecyclerView.ViewHolder} of the given type to
   * represent an item.
   *
   * @param parent The ViewGroup into which the new View will be added after it is bound to an
   *     adapter position.
   * @param viewType The view type of the new View.
   * @return A new ViewHolder that holds a View of the given view type.
   */
  @NonNull
  @Override
  public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return ContractViewHolder.create(parent, this.onContractClickListener);
  }

  /**
   * Called by RecyclerView to display the data at the specified position.
   *
   * @param holder The ViewHolder which should be updated to represent the contents of the item at
   *     the given position in the data set.
   * @param position The position of the item within the adapter's data set.
   */
  @Override
  public void onBindViewHolder(ContractViewHolder holder, int position) {
    Contract current = getItem(position);
    holder.bind(current);
  }

  /**
   * Interface to implement normal and long ClickEvents on a Contract in the {@link
   * androidx.recyclerview.widget.RecyclerView}.
   */
  public interface OnContractClickListener {
    /**
     * Can be overridden to implement an onClick Event.
     *
     * @param position Position of the clicked item in the RecyclerView
     */
    void onContractClick(int position);

    /**
     * Can be overridden to implement an onLongClick Event.
     *
     * @param position Position of the clicked item in the RecyclerView
     */
    void onContractLongClick(int position);
  }

  /**
   * Anonymous {@link ItemCallback} subclass to implement the callbacks that are triggered when the
   * LiveData changes.
   */
  public static class ContractDiff extends ItemCallback<Contract> {

    /**
     * Defines how to compare two Contracts.
     *
     * @param oldItem The new Contract
     * @param newItem The old Contract
     * @return boolean which defines if the to be compared contracts are the same contract.
     */
    @Override
    public boolean areItemsTheSame(@NonNull Contract oldItem, @NonNull Contract newItem) {
      return oldItem.getId() == newItem.getId();
    }

    /**
     * Defines how to compare the content two Contracts.
     *
     * @param oldItem The new Contract
     * @param newItem The old Contract
     * @return boolean which defines if the to be compared contracts have the same content.
     */
    @Override
    public boolean areContentsTheSame(@NonNull Contract oldItem, @NonNull Contract newItem) {
      return oldItem.equals(newItem);
    }
  }
}
