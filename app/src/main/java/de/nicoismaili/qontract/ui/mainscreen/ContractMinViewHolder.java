package de.nicoismaili.qontract.ui.mainscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.nicoismaili.qontract.R;
import de.nicoismaili.qontract.data.contract.pojo.ContractMin;

class ContractMinViewHolder extends RecyclerView.ViewHolder {
    private final TextView modelNameView;
    private final TextView locationView;
    private final TextView dateView;

    private ContractMinViewHolder(View itemView) {
        super(itemView);
        modelNameView = itemView.findViewById(R.id.model_name_view);
        locationView = itemView.findViewById(R.id.location_view);
        dateView = itemView.findViewById(R.id.date_view);
    }

    static ContractMinViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ContractMinViewHolder(view);
    }

    public void bind(ContractMin contractMin) {
        modelNameView.setText(String.format("%s %s", contractMin.getModelFirstname(), contractMin.getModelLastname()));
        locationView.setText(contractMin.getLocation());
        dateView.setText(contractMin.getDate().toString());
    }
}
