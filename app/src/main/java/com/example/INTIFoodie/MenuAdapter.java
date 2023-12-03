package com.example.INTIFoodie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private List<MenuModel> menuList;
    private OnAddItemClickListener onAddItemClickListener;
    private OnCartItemCLickListener onCartItemCLickListener;

    public MenuAdapter(List<MenuModel> menuList) {
        this.menuList = menuList;
    }

    public MenuModel getItem(int position) {
        if (position < 0 || position >= menuList.size()) {
            return null;
        }
        return menuList.get(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuModel menu = menuList.get(position);
        holder.foodNameTextView.setText(menu.getName());
        holder.priceNameTextView.setText(String.format(Locale.getDefault(), "RM%.2f", menu.getPrice()));
        holder.quantityTextView.setText(String.valueOf(menu.getQuantity()));

        // Load and display the image using Glide
        Glide.with(holder.itemView.getContext())
                .load(menu.getImageUrl())
                .into(holder.foodImageView);

        holder.addOn.setOnClickListener(v -> {
            menu.setQuantity(menu.getQuantity() + 1);
            holder.quantityTextView.setText(String.valueOf(menu.getQuantity()));
            // Update the total price based on the new quantity
            holder.priceNameTextView.setText(String.format(Locale.getDefault(), "RM%.2f", menu.getTotalPrice()));
        });

        holder.delete.setOnClickListener(v -> {
            if (menu.getQuantity() > 0) {
                menu.setQuantity(menu.getQuantity() - 1);
                holder.quantityTextView.setText(String.valueOf(menu.getQuantity()));
                // Update the total price based on the new quantity
                holder.priceNameTextView.setText(String.format(Locale.getDefault(), "RM%.2f", menu.getTotalPrice()));
            }
        });

        // Set click listener for the "ADD" button
        holder.btnAdd.setOnClickListener(v -> {
            if (onAddItemClickListener != null) {
                onAddItemClickListener.onAddItemClick(position);
            }
        });

        // Set click listener for adding items to the cart
        holder.itemView.setOnClickListener(v -> {
            if (onCartItemCLickListener != null) {
                onCartItemCLickListener.onCartItemCLick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void setMenuItems(List<MenuModel> menuItems) {
        this.menuList = menuItems;
        notifyDataSetChanged();
    }

    public void setOnAddItemClickListener(OnAddItemClickListener listener) {
        this.onAddItemClickListener = listener;
    }

    public void setOnCartItemCLickListener(OnCartItemCLickListener listener) {
        this.onCartItemCLickListener = listener;
    }

    public interface OnAddItemClickListener {
        void onAddItemClick(int position);
    }

    public interface OnCartItemCLickListener {
        void onCartItemCLick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView foodNameTextView;
        TextView priceNameTextView;
        Button delete;
        TextView quantityTextView;
        Button btnAdd, addOn;

        public ViewHolder(View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            priceNameTextView = itemView.findViewById(R.id.priceNameTextView2);
            delete = itemView.findViewById(R.id.delete);
            addOn = itemView.findViewById(R.id.addOn);
            quantityTextView = itemView.findViewById(R.id.quantity);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
