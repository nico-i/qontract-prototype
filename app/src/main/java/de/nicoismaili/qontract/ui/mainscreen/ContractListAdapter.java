package de.nicoismaili.qontract.ui.mainscreen;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import de.nicoismaili.qontract.data.contract.pojo.ContractMin;

public class ContractListAdapter extends ListAdapter<ContractMin, ContractMinViewHolder> {

    public ContractListAdapter(@NonNull DiffUtil.ItemCallback<ContractMin> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ContractMinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ContractMinViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ContractMinViewHolder holder, int position) {
        ContractMin current = getItem(position);
        holder.bind(current.getModelFirstname());
    }

    public static class ContractDiff extends DiffUtil.ItemCallback<ContractMin> {

        @Override
        public boolean areItemsTheSame(@NonNull ContractMin oldItem, @NonNull ContractMin newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ContractMin oldItem, @NonNull ContractMin newItem) {
            return oldItem.getModelLastname().equals(newItem.getModelLastname());
        }
    }


}
