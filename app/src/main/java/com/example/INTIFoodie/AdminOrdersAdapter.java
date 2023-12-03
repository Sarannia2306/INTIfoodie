package com.example.INTIFoodie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder> {

    private List<Order> adminOrdersList;

    public AdminOrdersAdapter(List<Order> adminOrdersList) {
        this.adminOrdersList = adminOrdersList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = adminOrdersList.get(position);

        holder.orderIDTextView.setText("Order ID: " + order.getOrderID());
        holder.totalAmountTextView.setText(String.format(Locale.getDefault(), "Total Amount: RM%.2f", order.getTotalAmount()));
    }

    @Override
    public int getItemCount() {
        return adminOrdersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIDTextView;
        TextView totalAmountTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIDTextView = itemView.findViewById(R.id.orderID);
            totalAmountTextView = itemView.findViewById(R.id.totalAmount);
        }
    }
}
