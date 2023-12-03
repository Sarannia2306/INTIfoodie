package com.example.INTIFoodie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

public class Wallet extends AppCompatActivity {

    private Button reloadButton;
    private EditText reloadPinEditText;
    private DatabaseReference userDatabase;

    private TextView reloadedAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

        reloadButton = findViewById(R.id.Reload);
        reloadPinEditText = findViewById(R.id.reloadPinEditText);
        reloadedAmountTextView = findViewById(R.id.reloadedAmount);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());
        }

        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadWallet();
            }
        });

        // Retrieve and display the reloaded amount from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("ReloadPrefs", MODE_PRIVATE);
        int reloadedAmount = preferences.getInt("ReloadAmount", 0); // Default value is 0
        reloadedAmountTextView.setText(String.format(Locale.getDefault(), "RM%d", reloadedAmount));
    }

    private void reloadWallet() {
        String enteredReloadPin = reloadPinEditText.getText().toString();

        if (enteredReloadPin.isEmpty()) {
            Toast.makeText(this, "Please enter a reload PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the entered reload PIN as a string
        String reloadAmountString = enteredReloadPin.substring(0, enteredReloadPin.length() - 7); // Remove the last 7 characters
        int reloadAmount;

        try {
            reloadAmount = Integer.parseInt(reloadAmountString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid reload PIN. Please enter the PIN correctly", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the reload amount is valid
        if (reloadAmount != 10 && reloadAmount != 20 && reloadAmount != 50 && reloadAmount != 100) {
            Toast.makeText(this, "Invalid reload amount. Please enter the PIN correctly", Toast.LENGTH_SHORT).show();
            return;
        }

        // Define hardcoded reload pins
        Map<Integer, String> hardcodedPins = new HashMap<>();
        hardcodedPins.put(10, "106667789");
        hardcodedPins.put(20, "208886799");
        hardcodedPins.put(50, "501118900");
        hardcodedPins.put(100, "1003339011");

        // Get the hardcoded pin based on the reload amount
        String expectedReloadPin = hardcodedPins.get(reloadAmount);

        // Check if the entered pin matches the expected pin
        if (!enteredReloadPin.equals(expectedReloadPin)) {
            Toast.makeText(this, "Incorrect reload PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reload the amount and store it in Firebase under the "Users" node
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userReloadsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("Reloads");

            // Check if there is an existing reload entry for the user
            userReloadsRef.orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Retrieve the current balance
                        Integer currentBalance = dataSnapshot.child("amount").getValue(Integer.class);

                        // Calculate the new balance by adding the current balance and the reload amount
                        int newBalance = currentBalance + reloadAmount;

                        // Update the existing reload entry with the new balance
                        dataSnapshot.getRef().child("amount").setValue(newBalance);

                        // Save the reloaded amount in SharedPreferences
                        saveReloadedAmount(newBalance);

                        // Update the reloaded amount TextView
                        reloadedAmountTextView.setText(String.format(Locale.getDefault(), "RM%d", newBalance));

                        Toast.makeText(Wallet.this, "Wallet reloaded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        // If there is no existing reload entry, create a new one
                        Map<String, Object> reloadData = new HashMap<>();
                        reloadData.put("amount", reloadAmount);

                        // Save reload data to Firebase without the timestamp
                        userReloadsRef.setValue(reloadData);

                        // Save the reloaded amount in SharedPreferences
                        saveReloadedAmount(reloadAmount);

                        // Update the reloaded amount TextView
                        reloadedAmountTextView.setText(String.format(Locale.getDefault(), "RM%d", reloadAmount));

                        Toast.makeText(Wallet.this, "Wallet reloaded successfully!", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled
                }
            });
        }
    }

    private void saveReloadedAmount(int reloadAmount) {
        // Use SharedPreferences to save the reloaded amount
        SharedPreferences preferences = getSharedPreferences("ReloadPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ReloadAmount", reloadAmount);
        editor.apply();
    }
}
