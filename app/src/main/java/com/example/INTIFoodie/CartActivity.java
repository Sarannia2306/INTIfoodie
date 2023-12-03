package com.example.INTIFoodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Random;
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

public class CartActivity extends AppCompatActivity {
    private static CartActivity instance;

    public static CartActivity getInstance() {
        return instance;
    }

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private Button btnCheckout;
    private DatabaseReference userDatabase;
    private FirebaseAuth auth;
    private List<CartItem> cartItems;

    private TextView totalAmountTextView;
    private double walletBalance = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        instance = this;

        recyclerView = findViewById(R.id.ViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItems = CartManager.getInstance().getCartItems();

        cartAdapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnTotalAmountChangeListener(new CartAdapter.OnTotalAmountChangeListener() {


            @Override
            public void onTotalAmountChanged(double totalAmount) {
                // Update the total amount TextView or perform any other actions
                totalAmountTextView.setText(String.format(Locale.getDefault(), "Total: RM%.2f", totalAmount));
            }
        });

        totalAmountTextView = findViewById(R.id.CartTotal);

        btnCheckout = findViewById(R.id.btnCheckout);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        }

        // Set up a click listener for the Checkout button
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout();
            }
        });

        // Calculate and display the total amount
        calculateAndDisplayTotal();
    }
    private void calculateAndDisplayTotal() {
        // Calculate the total amount based on the selected items in the cart
        double totalAmount = calculateTotalAmount();

        // Display the total amount in the TextView
        totalAmountTextView.setText("Total: RM" + totalAmount);
    }

    private double calculateTotalAmount() {
        // Iterate through the cart items and sum up the prices
        double totalAmount = 0.0;
        if (cartItems != null) {
            for (CartItem cartItem : cartItems) {
                totalAmount += cartItem.getPrice();
            }
        }
        return totalAmount;
    }

    private void checkout() {
        // Retrieve the user's wallet amount from Firebase
        userDatabase.child("Reloads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the user's wallet amount
                    Integer walletAmountObject = dataSnapshot.child("amount").getValue(Integer.class);

                    // Check if the wallet amount is not null before using it
                    if (walletAmountObject != null) {
                        int walletAmount = walletAmountObject.intValue();

                        // Calculate the total amount from the cart
                        double totalAmount = calculateTotalAmount();

                        // Check if the user has sufficient balance
                        if (walletAmount >= totalAmount) {
                            // Deduct the total amount from the user's wallet
                            int newWalletAmount = (int) (walletAmount - totalAmount);

                            // Generate a random 4-digit order ID
                            int orderID = generateOrderID();

                            // Update the user's wallet amount in Firebase
                            userDatabase.child("Reloads").child("amount").setValue(newWalletAmount);

                            // Clear the cart after successful checkout
                            clearCart();

                            // Start OrderSummaryActivity and pass the order ID and total amount
                            Intent intent = new Intent(CartActivity.this, OrderSummary.class);
                            intent.putExtra("orderID", orderID);
                            intent.putExtra("totalAmount", totalAmount);
                            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems));
                            startActivity(intent);

                            // Display a success message
                            Toast.makeText(CartActivity.this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Insufficient balance
                            Toast.makeText(CartActivity.this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }

    // Method to generate a random 4-digit order ID
    private int generateOrderID() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }


    private void clearCart() {
        // Clear the cart items
        CartManager.getInstance().clearCart();

        // Check if the cartAdapter is not null before notifying the adapter
        if (cartAdapter != null) {
            // Notify the adapter that the data set has changed
            cartAdapter.updateCartItems(new ArrayList<>());
        }
    }


}
