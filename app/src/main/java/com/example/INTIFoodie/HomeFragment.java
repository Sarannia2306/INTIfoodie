package com.example.INTIFoodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private TextView loginIcon;
    private ImageView appLogoImageView;
    private DatabaseReference logoRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        loginIcon = view.findViewById(R.id.loginIcon);
        appLogoImageView = view.findViewById(R.id.applogo);

        // Initialize Firebase Database reference for logo
        logoRef = FirebaseDatabase.getInstance().getReference().child("Logo");

        // Retrieve app logo from Firebase and update the ImageView
        retrieveAppLogoFromFirebase();

        // Set a click listener for the loginIcon button
        loginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the Login activity using the hosting activity's context
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void retrieveAppLogoFromFirebase() {
        logoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded() && dataSnapshot.exists()) {
                    String imageUrl = dataSnapshot.child("image").getValue(String.class);

                    // Load and display the logo using Glide if the fragment is still attached
                    if (getActivity() != null) {
                        Glide.with(requireContext())
                                .load(imageUrl)
                                .into(appLogoImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
            }
        });
    }


    public void onAdminLoginSuccess() {
        if (loginIcon != null) {
            loginIcon.setText("Admin");
        }
    }
}
