package com.example.INTIFoodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements MenuAdapter.OnAddItemClickListener {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private CartAdapter cartAdapter;
    private DatabaseReference menuRef;
    private List<CartItem> cartItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize cartItems
        cartItems = new ArrayList<>();

        // Set the OnAddItemClickListener to handle "ADD" button clicks
        menuAdapter = new MenuAdapter(new ArrayList<>());
        menuAdapter.setOnAddItemClickListener(this);

        recyclerView.setAdapter(menuAdapter);

        // Initialize the CartAdapter with an empty list
        cartAdapter = new CartAdapter(new ArrayList<>());

        menuRef = FirebaseDatabase.getInstance().getReference().child("Category");

        retrieveMenuItemsFromFirebase();

        // Inside onCreateView
        ImageView cartImageView = view.findViewById(R.id.cart);
        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void retrieveMenuItemsFromFirebase() {
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MenuModel> menuItems = new ArrayList<>();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String name = itemSnapshot.child("Name").getValue(String.class);
                    String image = itemSnapshot.child("Image").getValue(String.class);
                    double price = itemSnapshot.child("Price").getValue(Double.class);

                    MenuModel menuItem = new MenuModel(name, image, price, 0);
                    menuItems.add(menuItem);
                }

                // Set the menu items in the MenuAdapter
                menuAdapter.setMenuItems(menuItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
            }
        });
    }

    @Override
    public void onAddItemClick(int position) {
        MenuModel selectedItem = menuAdapter.getItem(position);
        if (selectedItem != null) {
            CartItem cartItem = new CartItem(selectedItem.getName(), selectedItem.getPrice(), 1);
            CartManager.getInstance().getCartItems().add(cartItem);
        }

        cartAdapter.updateCartItems(CartManager.getInstance().getCartItems());
    }

}
