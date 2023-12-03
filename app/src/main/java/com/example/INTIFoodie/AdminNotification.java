package com.example.INTIFoodie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// MainActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNotification extends AppCompatActivity {

    private EditText editText;
    private Button sendButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_noti);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("messages");

        // Initialize UI components
        editText = findViewById(R.id.editText);
        sendButton = findViewById(R.id.sendButton);

        // Set click listener for the sendButton
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToFirebase();
            }
        });
    }

    private void sendMessageToFirebase() {
        // Get the text from the EditText
        String message = editText.getText().toString().trim();

        // Check if the message is not empty
        if (!message.isEmpty()) {
            // Push the message to Firebase
            databaseReference.push().setValue(message);

            // Clear the EditText
            editText.setText("");
        }
    }
}
