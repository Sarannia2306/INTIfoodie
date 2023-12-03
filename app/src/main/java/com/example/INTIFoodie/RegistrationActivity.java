package com.example.INTIFoodie;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button registerBtn;
    FirebaseAuth mAuth;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), com.example.INTIFoodie.RegistrationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        registerBtn = findViewById(R.id.registerBtn);
        textView = findViewById(R.id.loginBtn);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });


        registerBtn.setOnClickListener(view -> {
            String email, password;
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(email)){
                Toast.makeText(RegistrationActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)){
                Toast.makeText(RegistrationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals("adminINTIMA@gmail.com") && password.equals("230601")) {
                // Redirect to the AdminHomePage class
                Intent intent = new Intent(getApplicationContext(), AdminHomePage.class);
                startActivity(intent);
                finish();
            } else {
                // For non-admin users, attempt to create an account
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Account Created",
                                        Toast.LENGTH_SHORT).show();
                                // Redirect to the PersonalInformation class
                                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Authentication Failed: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                Log.e("RegistrationActivity", "Authentication Failed", task.getException());
                            }
                        });
            }
        });
    }
    public void ShowHidePass(View view) {
        TextInputEditText password = findViewById(R.id.password);

        if (view.getId() == R.id.pass_visible) {
            if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) view).setImageResource(R.drawable.visibility_off_24);

                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) view).setImageResource(R.drawable.visibility_24);

                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}