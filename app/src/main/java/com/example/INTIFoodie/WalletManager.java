package com.example.INTIFoodie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Random;

public class WalletManager {

    private double walletBalance;
    private DatabaseReference userRef;
    private Context context;

    public WalletManager(Context context) {
        this.context = context;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("walletBalance");

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        walletBalance = snapshot.getValue(Double.class);
                        updateWalletBalance();  // Update the UI when wallet balance changes
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle errors or inform the user about the issue
                }
            });
        }
    }

    public void makePaymentAndProceed(double totalAmount) {
        if (walletBalance >= totalAmount) {
            // Assuming you have a method to deduct the amount from Firebase Wallet balance
            deductAmountFromWallet(totalAmount);

            saveOrderNumber();

            // Assuming that OrderSummaryActivity is the next step
            Intent intent = new Intent(context, OrderSummary.class);
            intent.putExtra("TOTAL_AMOUNT", totalAmount);
            context.startActivity(intent);
            ((Activity) context).finish(); // Finish the current activity after proceeding to the next one
        } else {
            Toast.makeText(context, "Wallet is insufficient, Please reload", Toast.LENGTH_SHORT).show();
        }
    }

    private void deductAmountFromWallet(double amount) {
        // Deduct the amount from Firebase Wallet balance
        walletBalance -= amount;
        userRef.setValue(walletBalance);

        // Update the UI to show the new balance
        updateWalletBalance();
    }



    private void updateWalletBalance() {
        // Implement the logic to update the wallet balance display
        // Assuming that you have a TextView named walletBalanceTextView in your layout
        TextView walletBalanceTextView = ((Activity) context).findViewById(R.id.reloadPinEditText);
        if (walletBalanceTextView != null) {
            walletBalanceTextView.setText(String.format(Locale.getDefault(), "RM%.2f", walletBalance));
        }
    }

    private void saveOrderNumber() {
        // Generate a random order number
        int orderNumber = generateOrderNumber();

        // Save the order number in the "orderNumber" node under the "users" node in Firebase
        if (userRef != null) {
            userRef.child("orderNumber").setValue(orderNumber);

            // Show a toast message (you can remove this if not needed)
            Toast.makeText(context, "Order Number: " + orderNumber, Toast.LENGTH_SHORT).show();
        } else {
            // Log an error or handle the situation where userRef is null
            Log.e("WalletManager", "userRef is null");
        }
    }

    private int generateOrderNumber() {
        // Generate a random 4-digit order number
        return new Random().nextInt(9000) + 1000;
    }
}
