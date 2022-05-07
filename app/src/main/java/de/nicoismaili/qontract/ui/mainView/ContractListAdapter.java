package de.nicoismaili.qontract.ui.mainView;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import de.nicoismaili.qontract.data.contract.pojo.ContractMin;

public class ContractListAdapter extends ListAdapter<ContractMin, ContractMinViewHolder> {
    private ContractListAdapter.OnContractClickListener onContractClickListener;

    public ContractListAdapter(@NonNull DiffUtil.ItemCallback<ContractMin> diffCallback, OnContractClickListener onContractClickListener) {
        super(diffCallback);
        this.onContractClickListener = onContractClickListener;
    }

    @NonNull
    @Override
    public ContractMinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ContractMinViewHolder.create(parent, this.onContractClickListener);
    }

    @Override
    public void onBindViewHolder(ContractMinViewHolder holder, int position) {
        ContractMin current = getItem(position);
        holder.bind(current);
    }

    public static class ContractDiff extends DiffUtil.ItemCallback<ContractMin> {

        @Override
        public boolean areItemsTheSame(@NonNull ContractMin oldItem, @NonNull ContractMin newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ContractMin oldItem, @NonNull ContractMin newItem) {
            return oldItem.getModelLastname().equals(newItem.getModelLastname()) && oldItem.getModelFirstname().equals(newItem.getModelFirstname()) && oldItem.getDate().equals(newItem.getDate());
        }
    }

    public interface OnContractClickListener {
        void onContractClick(int position);

        void onContractLongClick(int position);
    }
}
