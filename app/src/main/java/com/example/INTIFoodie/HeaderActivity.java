package com.example.INTIFoodie;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HeaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);

        // Your back icon
        ImageButton backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to the previous activity
                onBackPressed();
            }
        });

        // Your login icon
        ImageButton loginIcon = findViewById(R.id.loginIcon);
        loginIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the Login activity
                Intent intent = new Intent(HeaderActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
