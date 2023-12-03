package com.example.INTIFoodie;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartItem> cartItems;
    private OnTotalAmountChangeListener onTotalAmountChangeListener;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.itemNameTextView.setText(cartItem.getItemName());
        holder.priceTextView.setText(String.format(Locale.getDefault(), "RM%.2f", cartItem.getPrice()));
        holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartItems == null ? 0 : cartItems.size();
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

    public void updateCartItems(List<CartItem> updatedCartItems) {
        this.cartItems = updatedCartItems;
        notifyDataSetChanged();
    }

    // Interface to communicate total amount changes to the activity/fragment
    public interface OnTotalAmountChangeListener {
        void onTotalAmountChanged(double totalAmount);
    }

    public void setOnTotalAmountChangeListener(OnTotalAmountChangeListener listener) {
        this.onTotalAmountChangeListener = listener;
    }

    public void notifyTotalAmountChanged(double totalAmount) {
        // Notify any listeners about the total amount change
        if (onTotalAmountChangeListener != null) {
            onTotalAmountChangeListener.onTotalAmountChanged(totalAmount);
        }
    }
}
