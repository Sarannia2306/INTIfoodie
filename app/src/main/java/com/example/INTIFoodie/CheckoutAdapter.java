package com.example.INTIFoodie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    private List<MenuModel> items;

    public CheckoutAdapter(List<MenuModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuModel item = items.get(position);
        holder.nameTextView.setText(item.getName());
        holder.unitPriceTextView.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice()));
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, unitPriceTextView, quantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            unitPriceTextView = itemView.findViewById(R.id.unitPriceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }
    }
}
