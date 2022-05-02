package de.nicoismaili.qontract.ui.mainscreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.nicoismaili.qontract.R;
import de.nicoismaili.qontract.data.contract.pojo.ContractMin;

class ContractMinViewHolder extends RecyclerView.ViewHolder {
    private final TextView modelNameView;
    private final TextView locationView;
    private final TextView dateView;
    private final ImageView signedView;

    private ContractMinViewHolder(View itemView) {
        super(itemView);
        modelNameView = itemView.findViewById(R.id.model_name_view);
        locationView = itemView.findViewById(R.id.location_view);
        dateView = itemView.findViewById(R.id.date_view);
        signedView = itemView.findViewById(R.id.signed_icon);
    }

    static ContractMinViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ContractMinViewHolder(view);
    }

    public void bind(ContractMin contractMin) {
        modelNameView.setText(String.format("%s %s", contractMin.getModelFirstname(), contractMin.getModelLastname()));
        locationView.setText(contractMin.getLocation());
        Date date = contractMin.getDate();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
        dateView.setText(dateFormat.format(date));
        signedView.setBackgroundResource(contractMin.isSigned() ? R.drawable.ic_signed : R.drawable.ic_unsigned);
    }
}
