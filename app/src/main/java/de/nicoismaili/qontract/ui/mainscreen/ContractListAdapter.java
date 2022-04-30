package de.nicoismaili.qontract.ui.mainscreen;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import de.nicoismaili.qontract.data.contract.pojo.Contract;

public class ContractListAdapter extends ListAdapter<Contract, ContractViewHolder> {

    public ContractListAdapter(@NonNull DiffUtil.ItemCallback<Contract> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ContractViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ContractViewHolder holder, int position) {
        Contract current = getItem(position);
        holder.bind(current.getModelFirstname());
    }

    public static class ContractDiff extends DiffUtil.ItemCallback<Contract> {

        @Override
        public boolean areItemsTheSame(@NonNull Contract oldItem, @NonNull Contract newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contract oldItem, @NonNull Contract newItem) {
            return oldItem.getModelLastname().equals(newItem.getModelLastname());
        }
    }


}
