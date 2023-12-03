package com.example.INTIFoodie;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.INTIFoodie.databinding.ActivityNavigationBinding;

public class Navigation_bottom extends AppCompatActivity {

    private ActivityNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.navView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navigation_menu) {
                replaceFragment(new MenuFragment());
            } else if (itemId == R.id.navigation_notifications) {
                // Load NotificationsFragment
                replaceFragment(new NotificationsFragment());
            } else if (itemId == R.id.navigation_home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.navigation_me) {
                replaceFragment(new Me_Fragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_navigation, fragment);
        fragmentTransaction.commit();
    }
}
