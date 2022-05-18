package de.nicoismaili.qontract.ui.contractlist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import de.nicoismaili.qontract.data.contract.pojo.Contract;

public class ContractListAdapter extends ListAdapter<Contract, ContractViewHolder> {
    private final ContractListAdapter.OnContractClickListener onContractClickListener;

    public ContractListAdapter(@NonNull DiffUtil.ItemCallback<Contract> diffCallback, OnContractClickListener onContractClickListener) {
        super(diffCallback);
        this.onContractClickListener = onContractClickListener;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ContractViewHolder.create(parent, this.onContractClickListener);
    }

    @Override
    public void onBindViewHolder(ContractViewHolder holder, int position) {
        Contract current = getItem(position);
        holder.bind(current);
    }

    public static class ContractDiff extends DiffUtil.ItemCallback<Contract> {

        @Override
        public boolean areItemsTheSame(@NonNull Contract oldItem, @NonNull Contract newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contract oldItem, @NonNull Contract newItem) {
            return oldItem.equals(newItem);
        }
    }

    public interface OnContractClickListener {
        void onContractClick(int position);
        void onContractLongClick(int position);
    }
}
