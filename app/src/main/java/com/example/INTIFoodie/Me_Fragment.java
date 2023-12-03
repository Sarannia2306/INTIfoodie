package com.example.INTIFoodie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.Objects;

public class Me_Fragment extends Fragment {

    private TextView Name;
    private TextView Age;
    private TextView Identity;
    private TextView Wallet;
    private TextView UserID;
    private ImageView logoutIcon;

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        Name = view.findViewById(R.id.name);
        Age = view.findViewById(R.id.age);
        Identity = view.findViewById(R.id.identity);
        UserID = view.findViewById(R.id.userID);
        Wallet = view.findViewById(R.id.wallet);
        logoutIcon = view.findViewById(R.id.logoutIcon);

        FirebaseAuth dbAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = dbAuth.getCurrentUser();

        if (currentUser != null) {
            String user_id = currentUser.getUid();
            Log.d("Firebase", "User ID: " + user_id);

            // Reference to "User Details" under the current user
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("User Details");

            // Add a ValueEventListener to retrieve and update data
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Use the UserInfoClass to map the data from the database
                        UserInfoClass userInfo = dataSnapshot.getValue(UserInfoClass.class);

                        if (userInfo != null) {
                            // Update the TextViews with the retrieved data
                            Name.setText("Name: " + userInfo.getName());
                            Age.setText("Age: " + userInfo.getAge());
                            Identity.setText("Identity: " + userInfo.getIdentity());
                            UserID.setText("User ID: " + userInfo.getUserID());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error: " + databaseError.getMessage());
                }
            });
        } else {
            // Handle the case where the user is not authenticated or has no user ID
            Log.e("Firebase", "User is not authenticated or has no user ID");
        }

        Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on Wallet TextView
                Intent intent = new Intent(getActivity(), Wallet.class);
                startActivity(intent);
            }
        });

        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out the user
                FirebaseAuth.getInstance().signOut();
                // Navigate to the LoginActivity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}