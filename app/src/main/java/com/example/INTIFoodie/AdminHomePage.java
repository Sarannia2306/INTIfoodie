package com.example.INTIFoodie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHomePage extends AppCompatActivity {
    ImageView logoutIcon;
    ImageView orders;
    ImageView sendNoti;
    TextView UserDetails;
    AdminViewUserMyAdapter viewUserAdapter;
    RecyclerView AdminviewUser;
    List<AdminViewUserDataClass> viewUserdataList;

    ValueEventListener ViewUsereventListener;

    DatabaseReference databaseReference;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_homepage);

        logoutIcon = findViewById(R.id.logoutIcon);
        UserDetails = findViewById(R.id.UserDetails);
        AdminviewUser = findViewById(R.id.adminviewusers);
        sendNoti = findViewById(R.id.sendNoti);
        orders = findViewById(R.id.orders);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(AdminHomePage.this, 1);
        AdminviewUser.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomePage.this);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        viewUserdataList = new ArrayList<>();

        viewUserAdapter = new AdminViewUserMyAdapter(AdminHomePage.this, viewUserdataList);
        AdminviewUser.setAdapter(viewUserAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        dialog.show();
        ViewUsereventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewUserdataList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    // Change the key to match your data structure
                    String userId = userSnapshot.getKey();
                    DataSnapshot userInfoSnapshot = userSnapshot.child("User Details");

                    if (userInfoSnapshot.exists()) {
                        AdminViewUserDataClass dataClass = userInfoSnapshot.getValue(AdminViewUserDataClass.class);

                        if (dataClass != null) {
                            dataClass.setKey(userId);

                            viewUserdataList.add(dataClass);
                        }
                    }
                }
                viewUserAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        logoutIcon.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHomePage.this, LoginActivity.class);
            startActivity(intent);
        });

        UserDetails.setOnClickListener(view -> {
            // You might want to refresh the user list here if needed
            viewUserdataList.clear();
            viewUserAdapter.notifyDataSetChanged();
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, AdminOrders.class);
                startActivity(intent);
            }
        });

        // Set click listener for the sendNoti ImageView
        sendNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminNotificationActivity();
            }
        });
    }

    private void openAdminNotificationActivity() {
        // Create an Intent to open AdminNotificationActivity
        Intent intent = new Intent(AdminHomePage.this, AdminNotification.class);
        startActivity(intent);
    }
}

