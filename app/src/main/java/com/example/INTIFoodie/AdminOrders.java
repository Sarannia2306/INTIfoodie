package com.example.INTIFoodie;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminOrders extends AppCompatActivity {

    private RecyclerView adminOrders;
    private AdminOrdersAdapter adminOrdersAdapter;
    private List<Order> adminOrdersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_orders);

        // Initialize RecyclerView and its adapter
        adminOrders = findViewById(R.id.adminOrders);
        adminOrders.setLayoutManager(new LinearLayoutManager(this));
        adminOrdersList = new ArrayList<>();
        adminOrdersAdapter = new AdminOrdersAdapter(adminOrdersList);
        adminOrders.setAdapter(adminOrdersAdapter);

        // Retrieve orders from Firebase and update the RecyclerView
        retrieveOrdersFromFirebase();
    }

    private void retrieveOrdersFromFirebase() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminOrdersList.clear(); // Clear existing data
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null) {
                        adminOrdersList.add(order);
                    }
                }
                adminOrdersAdapter.notifyDataSetChanged(); // Notify adapter about the data change
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }
}
