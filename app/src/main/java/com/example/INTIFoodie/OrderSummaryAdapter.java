package com.example.INTIFoodie;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;
public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {
    private List<CartItem> orderItems;

    // Constructor without totalAmount and orderId
    public OrderSummaryAdapter(List<CartItem> cartItems) {
        this.orderItems = cartItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem orderItem = orderItems.get(position);

        holder.itemNameTextView.setText(orderItem.getItemName());
        holder.priceTextView.setText(String.format(Locale.getDefault(), "RM%.2f", orderItem.getPrice()));
        holder.quantityTextView.setText(String.valueOf(orderItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView priceTextView;
        TextView quantityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.textViewCartItemName);
            priceTextView = itemView.findViewById(R.id.textViewCartItemPrice);
            quantityTextView = itemView.findViewById(R.id.textViewCartItemQuantity);
        }
    }
}

