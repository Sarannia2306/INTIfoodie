package com.example.INTIFoodie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderSummary extends AppCompatActivity {

    private RecyclerView orderSummaryRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView orderIDTextView;
    private TextView totalAmountTextView;
    private TextView balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_summary);

        // Initialize your RecyclerView, orderItems, and orderSummaryAdapter
        orderSummaryRecyclerView = findViewById(R.id.orderSummaryRecyclerView);
        orderSummaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your TextViews
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        balanceTextView = findViewById(R.id.Balance_TextView);
        orderIDTextView = findViewById(R.id.orderIDTextView);

        // Initialize or update the cartAdapter
        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(cartItems);
            orderSummaryRecyclerView.setAdapter(cartAdapter);
        } else {
            cartAdapter.updateCartItems(cartItems);
            cartAdapter.notifyDataSetChanged();
        }

        // Get the order ID and total amount from the intent
        Intent intent = getIntent();
        int orderID = intent.getIntExtra("orderID", 0);
        double totalAmount = intent.getDoubleExtra("totalAmount", 0.0);
        ArrayList<CartItem> cartItems = intent.getParcelableArrayListExtra("cartItems");

        // Set up RecyclerView for cart items
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        orderSummaryRecyclerView.setLayoutManager(layoutManager);

        OrderSummaryAdapter orderSummaryAdapter = new OrderSummaryAdapter(cartItems);
        orderSummaryRecyclerView.setAdapter(orderSummaryAdapter);

        // Display the total amount in the TextView
        totalAmountTextView.setText(String.format(Locale.getDefault(), "Total Amount: RM%.2f", totalAmount));

        // Display the order ID in the TextView
        orderIDTextView.setText("Order ID: " + orderID);

        // Retrieve the user's balance from Firebase and set it in the balance TextView
        retrieveUserBalanceFromFirebase();

        // Save the order to Firebase
        saveOrderToFirebase(orderID, totalAmount, cartItems);
    }

    private void saveOrderToFirebase(int orderID, double totalAmount, ArrayList<CartItem> cartItems) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

            // Create an Order object
            Order order = new Order();
            order.setOrderID(String.valueOf(orderID));
            order.setTotalAmount(totalAmount);
            order.setUserID(currentUser.getUid());
            order.setItems(cartItems);

            // Save the order to Firebase
            ordersRef.push().setValue(order);
        }
    }

    private void retrieveUserBalanceFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userReloadsRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(currentUser.getUid())
                    .child("Reloads")
                    .child("amount");

            userReloadsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve the user's balance
                        Integer userBalance = dataSnapshot.getValue(Integer.class);

                        // Check if the userBalance is not null before using it
                        if (userBalance != null) {
                            // Update the balance TextView based on the retrieved balance
                            balanceTextView.setText("Balance: RM" + userBalance);
                        } else {
                            // Handle the case where userBalance is null
                            balanceTextView.setText("Balance: Not available");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled
                }
            });
        }
    }
}
