package de.nicoismaili.qontract.ui.mainscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import de.nicoismaili.qontract.R;

class ContractMinViewHolder extends RecyclerView.ViewHolder {
    private final TextView contractItemView;

    private ContractMinViewHolder(View itemView) {
        super(itemView);
        contractItemView = itemView.findViewById(R.id.textView);
    }

    static ContractMinViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ContractMinViewHolder(view);
    }

    public void bind(String text) {
        contractItemView.setText(text);
    }
}
