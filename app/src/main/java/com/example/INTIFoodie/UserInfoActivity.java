package com.example.INTIFoodie;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class UserInfoActivity extends AppCompatActivity {

    TextInputEditText name, age, userID;
    Spinner identity;
    Button btn_userinfosave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth dbAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.user_info);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        userID = findViewById(R.id.userID);
        identity = findViewById(R.id.identity);
        btn_userinfosave = findViewById(R.id.btn_userinfoSave);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Identity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        identity.setAdapter(adapter);

        btn_userinfosave.setOnClickListener(view -> {
            String Name = Objects.requireNonNull(name.getText()).toString();
            String Age = Objects.requireNonNull(age.getText()).toString();
            String UserID = Objects.requireNonNull(userID.getText()).toString();
            String Identity = identity.getSelectedItem().toString();

            AdminViewUserDataClass dataClass = new AdminViewUserDataClass(Name, Age, UserID, Identity);
            String user_id = Objects.requireNonNull(dbAuth.getCurrentUser()).getUid();

            FirebaseDatabase.getInstance().getReference("Users").child(user_id).child("User Details")
                    .setValue(dataClass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserInfoActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                            // Redirect to Me_Fragment after saving
                            Intent intent = new Intent(UserInfoActivity.this, Navigation_bottom.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(UserInfoActivity.this, Objects.requireNonNull(e.getMessage()),
                            Toast.LENGTH_SHORT).show());
        });
    }

}

